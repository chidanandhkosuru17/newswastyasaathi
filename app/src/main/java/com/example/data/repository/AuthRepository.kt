package com.example.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.data.model.WorkerUser
import com.example.data.sample.SyntheticData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthRepository(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("swasthya_auth_prefs", Context.MODE_PRIVATE)

    private val _currentUser = MutableStateFlow<WorkerUser?>(null)
    val currentUser: StateFlow<WorkerUser?> = _currentUser.asStateFlow()

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    init {
        val loggedIn = prefs.getBoolean("is_logged_in", false)
        if (loggedIn) {
            val id = prefs.getString("worker_id", "ASHA-101") ?: "ASHA-101"
            val name = prefs.getString("worker_name", "Anita Sharma") ?: "Anita Sharma"
            val email = prefs.getString("worker_email", "asha.anita@chandrapuri.health") ?: "asha.anita@chandrapuri.health"
            val village = prefs.getString("worker_village", "Amrapur") ?: "Amrapur"
            val role = prefs.getString("worker_role", "ASHA Worker") ?: "ASHA Worker"
            val user = WorkerUser(id, name, email, role, village)
            _currentUser.value = user
            _isLoggedIn.value = true
        }
    }

    enum class PasswordStrength {
        WEAK,
        MEDIUM,
        STRONG
    }

    companion object {
        fun calculatePasswordStrength(password: String): PasswordStrength {
            if (password.length < 6) return PasswordStrength.WEAK
            val hasLetters = password.any { it.isLetter() }
            val hasDigits = password.any { it.isDigit() }
            val hasSymbols = password.any { !it.isLetterOrDigit() }
            return when {
                password.length >= 8 && hasLetters && hasDigits && hasSymbols -> PasswordStrength.STRONG
                password.length >= 6 && hasLetters && (hasDigits || hasSymbols) -> PasswordStrength.MEDIUM
                else -> PasswordStrength.WEAK
            }
        }

        fun isValidEmailOrId(input: String): Boolean {
            val trimmed = input.trim()
            if (trimmed.isEmpty()) return false
            // Matches worker ID format (e.g. ASHA-101, ANM-202, SB-1234)
            if (Regex("^[A-Za-z]{2,8}-\\d{2,8}$").matches(trimmed)) return true
            // Matches standard email format
            return Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$").matches(trimmed)
        }

        fun isValidEmail(email: String): Boolean {
            val trimmed = email.trim()
            return Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$").matches(trimmed)
        }
    }

    fun calculatePasswordStrength(password: String): PasswordStrength = Companion.calculatePasswordStrength(password)
    fun isValidEmailOrId(input: String): Boolean = Companion.isValidEmailOrId(input)
    fun isValidEmail(email: String): Boolean = Companion.isValidEmail(email)

    suspend fun signUp(
        email: String,
        password: String,
        fullName: String,
        role: String,
        village: String,
        rememberMe: Boolean = true
    ): Result<WorkerUser> = kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {
        val trimmedEmail = email.trim()
        val trimmedName = fullName.trim()
        if (trimmedName.isEmpty()) {
            return@withContext Result.failure(IllegalArgumentException("Please enter your full name"))
        }
        if (!isValidEmail(trimmedEmail)) {
            return@withContext Result.failure(IllegalArgumentException("Please enter a valid email address"))
        }
        if (password.length < 6) {
            return@withContext Result.failure(IllegalArgumentException("Password must be at least 6 characters"))
        }

        // 1. Attempt Supabase GoTrue Auth Sign Up
        val supabaseResult = com.example.data.supabase.SupabaseClient.signUpWithSupabase(
            email = trimmedEmail,
            pass = password,
            fullName = trimmedName,
            role = role,
            village = village
        )

        // If duplicate user or explicit rejection from Supabase (not an offline network failure)
        if (!supabaseResult.isSuccess && !supabaseResult.isOfflineOrNetworkError) {
            val errMsg = supabaseResult.message ?: "Sign up failed on Supabase backend"
            return@withContext Result.failure(IllegalArgumentException(errMsg))
        }

        val generatedId = if (supabaseResult.isSuccess && !supabaseResult.userId.isNullOrEmpty()) {
            "SB-" + supabaseResult.userId.take(6).uppercase()
        } else {
            "WORKER-" + trimmedEmail.substringBefore("@").take(6).uppercase()
        }

        val newUser = WorkerUser(
            id = generatedId,
            name = trimmedName,
            email = trimmedEmail,
            role = role,
            assignedVillage = if (village.isBlank()) "Chandrapuri" else village
        )

        // Cache registered credentials locally for offline continuity
        saveRegisteredUserLocally(newUser, password)

        if (rememberMe) {
            prefs.edit()
                .putBoolean("is_logged_in", true)
                .putString("worker_id", newUser.id)
                .putString("worker_name", newUser.name)
                .putString("worker_email", newUser.email)
                .putString("worker_village", newUser.assignedVillage)
                .putString("worker_role", newUser.role)
                .apply()
        }

        _currentUser.value = newUser
        _isLoggedIn.value = true
        Result.success(newUser)
    }

    suspend fun signIn(idOrEmail: String, password: String, rememberMe: Boolean = true): Result<WorkerUser> = kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {
        val trimmed = idOrEmail.trim()
        if (!isValidEmailOrId(trimmed)) {
            return@withContext Result.failure(IllegalArgumentException("Invalid email format or Worker ID"))
        }
        if (password.length < 6) {
            return@withContext Result.failure(IllegalArgumentException("Password must be at least 6 characters"))
        }

        // Check local registered users first
        val localRegistered = getLocalRegisteredUser(trimmed)

        // 1. Attempt Supabase GoTrue Auth if input is an email
        var supabaseAuthSuccess = false
        var supabaseUserName: String? = null
        var supabaseUserRole: String? = null
        var supabaseUserVillage: String? = null

        if (trimmed.contains("@")) {
            val sbResult = com.example.data.supabase.SupabaseClient.signInWithSupabase(trimmed, password)
            if (sbResult.isSuccess) {
                supabaseAuthSuccess = true
                supabaseUserName = sbResult.fullName
                supabaseUserRole = sbResult.role
                supabaseUserVillage = sbResult.village
            } else if (!sbResult.isOfflineOrNetworkError) {
                // If Supabase returned 400 invalid credentials, check if it's a known demo worker
                val isDemo = SyntheticData.demoWorkers.any { it.email.equals(trimmed, ignoreCase = true) }
                val isLocalValid = localRegistered != null && localRegistered.second == password
                if (!isDemo && !isLocalValid) {
                    return@withContext Result.failure(IllegalArgumentException(sbResult.message ?: "Invalid email or password"))
                }
            }
        }

        // Match against Supabase profile, local registered cache, demo workers, or fallback
        val matched = when {
            supabaseAuthSuccess -> WorkerUser(
                id = "SB-" + trimmed.substringBefore("@").take(6).uppercase(),
                name = supabaseUserName ?: localRegistered?.first?.name ?: trimmed.substringBefore("@").replace(".", " ").capitalizeWords(),
                email = trimmed,
                role = supabaseUserRole ?: localRegistered?.first?.role ?: "Frontline Health Worker",
                assignedVillage = supabaseUserVillage ?: localRegistered?.first?.assignedVillage ?: "Amrapur"
            )
            localRegistered != null -> localRegistered.first
            else -> SyntheticData.demoWorkers.find {
                it.email.equals(trimmed, ignoreCase = true) || it.id.equals(trimmed, ignoreCase = true)
            } ?: WorkerUser(
                id = if (trimmed.contains("@")) "WORKER-" + trimmed.substringBefore("@").take(6).uppercase() else trimmed.uppercase(),
                name = if (trimmed.contains("@")) trimmed.substringBefore("@").replace(".", " ").capitalizeWords() else "Health Worker $trimmed",
                email = if (trimmed.contains("@")) trimmed else "$trimmed@chandrapuri.health",
                role = "Frontline Health Worker",
                assignedVillage = "Amrapur"
            )
        }

        if (rememberMe) {
            prefs.edit()
                .putBoolean("is_logged_in", true)
                .putString("worker_id", matched.id)
                .putString("worker_name", matched.name)
                .putString("worker_email", matched.email)
                .putString("worker_village", matched.assignedVillage)
                .putString("worker_role", matched.role)
                .apply()
        }

        _currentUser.value = matched
        _isLoggedIn.value = true
        Result.success(matched)
    }

    fun signOut() {
        prefs.edit()
            .putBoolean("is_logged_in", false)
            .remove("worker_id")
            .remove("worker_name")
            .remove("worker_email")
            .remove("worker_village")
            .remove("worker_role")
            .apply()
        _currentUser.value = null
        _isLoggedIn.value = false
    }

    private fun saveRegisteredUserLocally(user: WorkerUser, pass: String) {
        val existing = prefs.getString("registered_accounts", "") ?: ""
        val entry = "${user.id}|${user.email}|${user.name}|${user.role}|${user.assignedVillage}|$pass"
        val updated = if (existing.isEmpty()) entry else "$existing;;$entry"
        prefs.edit().putString("registered_accounts", updated).apply()
    }

    private fun getLocalRegisteredUser(emailOrId: String): Pair<WorkerUser, String>? {
        val existing = prefs.getString("registered_accounts", "") ?: ""
        if (existing.isEmpty()) return null
        val entries = existing.split(";;")
        for (item in entries) {
            val parts = item.split("|")
            if (parts.size >= 6) {
                val id = parts[0]
                val email = parts[1]
                val name = parts[2]
                val role = parts[3]
                val village = parts[4]
                val pass = parts[5]
                if (email.equals(emailOrId, ignoreCase = true) || id.equals(emailOrId, ignoreCase = true)) {
                    return Pair(WorkerUser(id, name, email, role, village), pass)
                }
            }
        }
        return null
    }

    private fun String.capitalizeWords(): String {
        return split(" ").joinToString(" ") { it.replaceFirstChar { c -> c.uppercase() } }
    }
}
