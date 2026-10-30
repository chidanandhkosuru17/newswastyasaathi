package com.example.data.supabase

import android.util.Log
import com.example.data.local.EmergencyCaseEntity
import com.example.data.local.SyncQueueEntity
import com.example.data.local.VisitItemEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

object SupabaseClient {

    private const val TAG = "SupabaseClient"

    data class ConnectionResult(
        val isConnected: Boolean,
        val message: String,
        val latencyMs: Long = 0,
        val statusCode: Int = 0
    )

    private val authInterceptor = Interceptor { chain ->
        val original = chain.request()
        val request = original.newBuilder()
            .header("apikey", SupabaseConfig.supabaseAnonKey)
            .header("Authorization", "Bearer ${SupabaseConfig.supabaseAnonKey}")
            .header("Content-Type", "application/json")
            .build()
        chain.proceed(request)
    }

    private val okHttpClient by lazy {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(logging)
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .build()
    }

    private val retrofit by lazy {
        val baseUrl = if (SupabaseConfig.supabaseUrl.endsWith("/")) {
            SupabaseConfig.supabaseUrl
        } else {
            "${SupabaseConfig.supabaseUrl}/"
        }
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    val api: SupabaseApiService by lazy {
        retrofit.create(SupabaseApiService::class.java)
    }

    suspend fun testConnection(): ConnectionResult = withContext(Dispatchers.IO) {
        val start = System.currentTimeMillis()
        try {
            val response = api.checkAuthHealth()
            val latency = System.currentTimeMillis() - start
            val code = response.code()
            if (response.isSuccessful) {
                ConnectionResult(
                    isConnected = true,
                    message = "Connected to Supabase GoTrue Auth (HTTP $code)",
                    latencyMs = latency,
                    statusCode = code
                )
            } else {
                ConnectionResult(
                    isConnected = false,
                    message = "Supabase server responded with HTTP $code: ${response.message()}",
                    latencyMs = latency,
                    statusCode = code
                )
            }
        } catch (e: Exception) {
            val latency = System.currentTimeMillis() - start
            Log.e(TAG, "Supabase connection test failed", e)
            ConnectionResult(
                isConnected = false,
                message = "Connection failed: ${e.localizedMessage ?: e.message}",
                latencyMs = latency,
                statusCode = -1
            )
        }
    }

    suspend fun uploadEmergencyCase(case: EmergencyCaseEntity): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val body = mapOf(
                "id" to case.id,
                "patient_name" to case.patientName,
                "age" to case.approximateAge,
                "gender" to case.gender,
                "village" to case.village,
                "contact_phone" to case.contactPhone,
                "emergency_type" to case.emergencyType,
                "danger_signs" to case.dangerSigns,
                "esc_citation" to case.escCitation,
                "vitals_notes" to case.vitalsNotes,
                "first_aid_given" to case.firstAidGiven,
                "referral_destination" to case.referralDestination,
                "recorded_at" to case.recordedAt,
                "status" to case.status
            )
            val response = api.insertEmergencyCase(body)
            if (response.isSuccessful || response.code() in 200..299) {
                Result.success(Unit)
            } else {
                val errorBody = response.errorBody()?.string().orEmpty()
                Log.w(TAG, "uploadEmergencyCase HTTP ${response.code()}: $errorBody")
                Result.failure(Exception("Supabase HTTP ${response.code()}: $errorBody"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "uploadEmergencyCase network error", e)
            Result.failure(e)
        }
    }

    suspend fun uploadVisits(visits: List<VisitItemEntity>): Result<Int> = withContext(Dispatchers.IO) {
        if (visits.isEmpty()) return@withContext Result.success(0)
        try {
            val bodyList = visits.map { v ->
                mapOf(
                    "id" to v.id,
                    "member_id" to v.memberId,
                    "household_id" to v.householdId,
                    "member_name" to v.memberName,
                    "village" to v.village,
                    "due_item" to v.dueItem,
                    "priority" to v.priority,
                    "due_date" to v.dueDate,
                    "is_completed" to v.isCompleted,
                    "completion_date" to v.completionDate,
                    "notes" to v.notes
                )
            }
            val response = api.insertVisits(bodyList)
            if (response.isSuccessful || response.code() in 200..299) {
                Result.success(visits.size)
            } else {
                val errorBody = response.errorBody()?.string().orEmpty()
                Log.w(TAG, "uploadVisits HTTP ${response.code()}: $errorBody")
                Result.failure(Exception("Supabase HTTP ${response.code()}: $errorBody"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "uploadVisits network error", e)
            Result.failure(e)
        }
    }

    suspend fun uploadSyncLogs(items: List<SyncQueueEntity>): Result<Int> = withContext(Dispatchers.IO) {
        if (items.isEmpty()) return@withContext Result.success(0)
        try {
            val bodyList = items.map { item ->
                mapOf(
                    "action_type" to item.actionType,
                    "payload" to item.payload,
                    "device_id" to "HEALTH_WORKER_APP"
                )
            }
            val response = api.insertSyncLogs(bodyList)
            if (response.isSuccessful || response.code() in 200..299) {
                Result.success(items.size)
            } else {
                val errorBody = response.errorBody()?.string().orEmpty()
                Log.w(TAG, "uploadSyncLogs HTTP ${response.code()}: $errorBody")
                Result.failure(Exception("Supabase HTTP ${response.code()}: $errorBody"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "uploadSyncLogs network error", e)
            Result.failure(e)
        }
    }

    data class SupabaseAuthResult(
        val isSuccess: Boolean,
        val token: String? = null,
        val userId: String? = null,
        val email: String? = null,
        val fullName: String? = null,
        val role: String? = null,
        val village: String? = null,
        val message: String? = null,
        val isOfflineOrNetworkError: Boolean = false
    )

    suspend fun signUpWithSupabase(
        email: String,
        pass: String,
        fullName: String,
        role: String,
        village: String
    ): SupabaseAuthResult = withContext(Dispatchers.IO) {
        try {
            val body = mapOf(
                "email" to email,
                "password" to pass,
                "data" to mapOf(
                    "full_name" to fullName,
                    "role" to role,
                    "village" to village
                )
            )
            val response = api.signUp(body)
            if (response.isSuccessful || response.code() in 200..201) {
                val rawBody = response.body()?.string().orEmpty()
                val userId = extractJsonString(rawBody, "id")
                SupabaseAuthResult(
                    isSuccess = true,
                    userId = userId,
                    email = email,
                    fullName = fullName,
                    role = role,
                    village = village,
                    message = "Account successfully registered on Supabase backend"
                )
            } else {
                val err = response.errorBody()?.string().orEmpty()
                val parsedError = extractJsonString(err, "msg")
                    ?: extractJsonString(err, "error_description")
                    ?: extractJsonString(err, "message")
                    ?: "Supabase registration failed (HTTP ${response.code()})"
                Log.w(TAG, "Supabase signUp HTTP ${response.code()}: $err")
                SupabaseAuthResult(
                    isSuccess = false,
                    message = parsedError
                )
            }
        } catch (e: Exception) {
            Log.e(TAG, "Supabase signUp network error", e)
            SupabaseAuthResult(
                isSuccess = false,
                isOfflineOrNetworkError = true,
                message = e.localizedMessage ?: "Network connection unavailable"
            )
        }
    }

    suspend fun signInWithSupabase(
        email: String,
        pass: String
    ): SupabaseAuthResult = withContext(Dispatchers.IO) {
        try {
            val body = mapOf(
                "email" to email,
                "password" to pass
            )
            val response = api.signInWithPassword(body)
            if (response.isSuccessful) {
                val rawBody = response.body()?.string().orEmpty()
                val token = extractJsonString(rawBody, "access_token")
                val userId = extractJsonString(rawBody, "id")
                val name = extractJsonString(rawBody, "full_name")
                val role = extractJsonString(rawBody, "role")
                val village = extractJsonString(rawBody, "village")
                SupabaseAuthResult(
                    isSuccess = true,
                    token = token,
                    userId = userId,
                    email = email,
                    fullName = name,
                    role = role,
                    village = village,
                    message = "Authenticated successfully with Supabase GoTrue"
                )
            } else {
                val err = response.errorBody()?.string().orEmpty()
                val parsedError = extractJsonString(err, "error_description")
                    ?: extractJsonString(err, "msg")
                    ?: extractJsonString(err, "message")
                    ?: "Authentication failed (HTTP ${response.code()})"
                Log.w(TAG, "Supabase signIn HTTP ${response.code()}: $err")
                SupabaseAuthResult(
                    isSuccess = false,
                    message = parsedError
                )
            }
        } catch (e: Exception) {
            Log.e(TAG, "Supabase signIn network error", e)
            SupabaseAuthResult(
                isSuccess = false,
                isOfflineOrNetworkError = true,
                message = e.localizedMessage ?: "Network connection unavailable"
            )
        }
    }

    suspend fun authenticateWithSupabase(email: String, pass: String): Result<String> {
        val res = signInWithSupabase(email, pass)
        return if (res.isSuccess) {
            Result.success(res.message ?: "Success")
        } else {
            Result.failure(Exception(res.message ?: "Authentication failed"))
        }
    }

    private fun extractJsonString(json: String, key: String): String? {
        val pattern = Regex("\"$key\"\\s*:\\s*\"([^\"]+)\"")
        return pattern.find(json)?.groupValues?.get(1)
    }
}
