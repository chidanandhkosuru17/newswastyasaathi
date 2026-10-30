package com.example.data.supabase

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface SupabaseApiService {

    @GET("auth/v1/health")
    suspend fun checkAuthHealth(): Response<ResponseBody>

    @GET("rest/v1/emergency_cases")
    suspend fun getEmergencyCases(
        @Query("limit") limit: Int = 10,
        @Query("order") order: String = "created_at.desc"
    ): Response<ResponseBody>

    @POST("rest/v1/emergency_cases")
    @Headers("Prefer: return=minimal")
    suspend fun insertEmergencyCase(@Body body: Map<String, Any?>): Response<ResponseBody>

    @POST("rest/v1/visits")
    @Headers("Prefer: return=minimal")
    suspend fun insertVisits(@Body body: List<Map<String, Any?>>): Response<ResponseBody>

    @POST("rest/v1/sync_logs")
    @Headers("Prefer: return=minimal")
    suspend fun insertSyncLogs(@Body body: List<Map<String, Any?>>): Response<ResponseBody>

    @POST("auth/v1/token?grant_type=password")
    suspend fun signInWithPassword(@Body credentials: Map<String, @JvmSuppressWildcards Any?>): Response<ResponseBody>

    @POST("auth/v1/signup")
    suspend fun signUp(@Body credentials: Map<String, @JvmSuppressWildcards Any?>): Response<ResponseBody>

    @POST("auth/v1/logout")
    suspend fun signOut(): Response<ResponseBody>
}
