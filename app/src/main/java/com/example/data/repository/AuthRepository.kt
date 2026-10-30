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
            val user = WorkerUser(id, name, email, "ASHA Worker", village)
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
            // Matches worker ID format (e.g. ASHA-101, ANM-202)
            if (Regex("^[A-Za-z]{3,5}-\\d{3,5}$").matches(trimmed)) return true
            // Matches standard email format
            return Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$").matches(trimmed)
        }
    }

    fun calculatePasswordStrength(password: String): PasswordStrength = Companion.calculatePasswordStrength(password)
    fun isValidEmailOrId(input: String): Boolean = Companion.isValidEmailOrId(input)

    fun signIn(idOrEmail: String, password: String, rememberMe: Boolean = true): Result<WorkerUser> {
        val trimmed = idOrEmail.trim()
        if (!isValidEmailOrId(trimmed)) {
            return Result.failure(IllegalArgumentException("Invalid email format or Worker ID"))
        }
        if (password.length < 6) {
            return Result.failure(IllegalArgumentException("Password must be at least 6 characters"))
        }

        // Match against known demo workers or allow valid health ID
        val matched = SyntheticData.demoWorkers.find {
            it.email.equals(trimmed, ignoreCase = true) || it.id.equals(trimmed, ignoreCase = true)
        } ?: WorkerUser(
            id = if (trimmed.contains("@")) "WORKER-" + trimmed.substringBefore("@").take(6).uppercase() else trimmed.uppercase(),
            name = if (trimmed.contains("@")) trimmed.substringBefore("@").replace(".", " ").capitalizeWords() else "Health Worker $trimmed",
            email = if (trimmed.contains("@")) trimmed else "$trimmed@chandrapuri.health",
            role = "Frontline Health Worker",
            assignedVillage = "Amrapur"
        )

        if (rememberMe) {
            prefs.edit()
                .putBoolean("is_logged_in", true)
                .putString("worker_id", matched.id)
                .putString("worker_name", matched.name)
                .putString("worker_email", matched.email)
                .putString("worker_village", matched.assignedVillage)
                .apply()
        }

        _currentUser.value = matched
        _isLoggedIn.value = true
        return Result.success(matched)
    }

    fun signOut() {
        prefs.edit().clear().apply()
        _currentUser.value = null
        _isLoggedIn.value = false
    }

    private fun String.capitalizeWords(): String {
        return split(" ").joinToString(" ") { it.replaceFirstChar { c -> c.uppercase() } }
    }
}
