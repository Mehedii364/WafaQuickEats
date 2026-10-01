package com.example.data.remote

import com.squareup.moshi.JsonClass
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

@JsonClass(generateAdapter = true)
data class ApiResponse<T>(
    val success: Boolean,
    val message: String,
    val data: T? = null,
    val errors: List<String>? = null
)

@JsonClass(generateAdapter = true)
data class GpsBatchRequest(
    val riderId: String,
    val orderId: String,
    val locations: List<GpsPointDto>
)

@JsonClass(generateAdapter = true)
data class GpsPointDto(
    val latitude: Double,
    val longitude: Double,
    val accuracy: Float,
    val speed: Float,
    val bearing: Float,
    val timestamp: Long
)

@JsonClass(generateAdapter = true)
data class UpdateOrderStatusRequest(
    val orderId: String,
    val newStatus: String,
    val actorId: String,
    val actorRole: String,
    val otp: String? = null,
    val note: String? = null
)

@JsonClass(generateAdapter = true)
data class AiTestRequest(
    val providerId: String,
    val prompt: String = "Test connection"
)

@JsonClass(generateAdapter = true)
data class AiTestResponse(
    val reachable: Boolean,
    val latencyMs: Long,
    val message: String
)

interface WafaApiService {
    @GET("api/health.php")
    suspend fun checkHealth(): Response<ApiResponse<Map<String, String>>>

    @POST("api/gps/sync_batch.php")
    suspend fun syncGpsBatch(
        @Header("Authorization") token: String,
        @Body request: GpsBatchRequest
    ): Response<ApiResponse<Map<String, Any>>>

    @POST("api/orders/update_status.php")
    suspend fun updateOrderStatus(
        @Header("Authorization") token: String,
        @Body request: UpdateOrderStatusRequest
    ): Response<ApiResponse<Map<String, Any>>>

    @POST("api/ai/test.php")
    suspend fun testAiProvider(
        @Header("Authorization") token: String,
        @Body request: AiTestRequest
    ): Response<ApiResponse<AiTestResponse>>
}
