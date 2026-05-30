package com.securityguard.app.network

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface GateApi {
  // TODO: Replace with actual API call — GET inward/
    @GET("inward/")
    suspend fun listInward(
        @Query("from_date") fromDate: String,
        @Query("to_date") toDate: String,
        @Query("status") status: String? = null,
        @Query("page") page: Int? = null,
        @Query("page_size") pageSize: Int? = null,
    ): Response<ApiEnvelope<Map<String, Any?>>>

  // TODO: Replace with actual API call — GET inward/{id}/
    @GET("inward/{id}/")
    suspend fun getInward(@Path("id") id: String): Response<ApiEnvelope<Map<String, Any?>>>

  // TODO: Replace with actual API call — POST inward/
    @Multipart
    @POST("inward/")
    suspend fun createInward(
        @Part parts: List<MultipartBody.Part>,
    ): Response<ApiEnvelope<Map<String, Any?>>>

  // TODO: Replace with actual API call — POST inward/{id}/upload-invoice/
    @Multipart
    @POST("inward/{id}/upload-invoice/")
    suspend fun uploadInwardInvoice(
        @Path("id") id: String,
        @Part invoice: MultipartBody.Part,
    ): Response<ApiEnvelope<Map<String, Any?>>>

  // TODO: Replace with actual API call — PATCH inward/{id}/confirm-invoice/
    @PATCH("inward/{id}/confirm-invoice/")
    suspend fun confirmInwardInvoice(
        @Path("id") id: String,
        @Body body: Map<String, Any?>,
    ): Response<ApiEnvelope<Map<String, Any?>>>

  // TODO: Replace with actual API call — POST inward/{id}/allow-in/
    @POST("inward/{id}/allow-in/")
    suspend fun allowInInward(
        @Path("id") id: String,
        @Body body: AllowInRequest,
    ): Response<ApiEnvelope<Map<String, Any?>>>

  // TODO: Replace with actual API call — POST inward/{id}/mark-exit/
    @POST("inward/{id}/mark-exit/")
    suspend fun markExitInward(
        @Path("id") id: String,
        @Body body: MarkExitRequest,
    ): Response<ApiEnvelope<Map<String, Any?>>>

  // TODO: Replace with actual API call — POST inward/{id}/approve/
    @POST("inward/{id}/approve/")
    suspend fun approveInward(@Path("id") id: String): Response<ApiEnvelope<Map<String, Any?>>>

  // TODO: Replace with actual API call — POST inward/{id}/reject/
    @POST("inward/{id}/reject/")
    suspend fun rejectInward(
        @Path("id") id: String,
        @Body body: Map<String, String>,
    ): Response<ApiEnvelope<Map<String, Any?>>>

  // TODO: Replace with actual API call — GET outward/
    @GET("outward/")
    suspend fun listOutward(
        @Query("from_date") fromDate: String,
        @Query("to_date") toDate: String,
        @Query("status") status: String? = null,
        @Query("type") type: String? = null,
        @Query("page") page: Int? = null,
        @Query("page_size") pageSize: Int? = null,
    ): Response<ApiEnvelope<Map<String, Any?>>>

  // TODO: Replace with actual API call — POST outward/
    @Multipart
    @POST("outward/")
    suspend fun createOutward(@Part parts: List<MultipartBody.Part>): Response<ApiEnvelope<Map<String, Any?>>>

  // TODO: Replace with actual API call — POST outward/{id}/allow-in/
    @POST("outward/{id}/allow-in/")
    suspend fun allowInOutward(
        @Path("id") id: String,
        @Body body: AllowInRequest,
    ): Response<ApiEnvelope<Map<String, Any?>>>

  // TODO: Replace with actual API call — POST outward/{id}/mark-exit/
    @POST("outward/{id}/mark-exit/")
    suspend fun markExitOutward(
        @Path("id") id: String,
        @Body body: MarkExitRequest,
    ): Response<ApiEnvelope<Map<String, Any?>>>

  // TODO: Replace with actual API call — GET visitors/
    @GET("visitors/")
    suspend fun listVisitors(
        @Query("from_date") fromDate: String,
        @Query("to_date") toDate: String,
        @Query("status") status: String? = null,
        @Query("page") page: Int? = null,
        @Query("page_size") pageSize: Int? = null,
    ): Response<ApiEnvelope<Map<String, Any?>>>

  // TODO: Replace with actual API call — POST visitors/
    @Multipart
    @POST("visitors/")
    suspend fun createVisitor(@Part parts: List<MultipartBody.Part>): Response<ApiEnvelope<Map<String, Any?>>>

  // TODO: Replace with actual API call — POST visitors/{id}/allow-in/
    @POST("visitors/{id}/allow-in/")
    suspend fun allowInVisitor(
        @Path("id") id: String,
        @Body body: AllowInRequest,
    ): Response<ApiEnvelope<Map<String, Any?>>>

  // TODO: Replace with actual API call — POST visitors/{id}/mark-exit/
    @POST("visitors/{id}/mark-exit/")
    suspend fun markExitVisitor(
        @Path("id") id: String,
        @Body body: MarkExitRequest,
    ): Response<ApiEnvelope<Map<String, Any?>>>

  // TODO: Replace with actual API call — GET returnable-return/
    @GET("returnable-return/")
    suspend fun listReturnableReturns(
        @Query("from_date") fromDate: String? = null,
        @Query("to_date") toDate: String? = null,
        @Query("status") status: String? = null,
        @Query("page") page: Int? = null,
        @Query("page_size") pageSize: Int? = null,
    ): Response<ApiEnvelope<Map<String, Any?>>>

  // TODO: Replace with actual API call — POST returnable-return/
    @POST("returnable-return/")
    suspend fun createReturnableReturn(@Body body: Map<String, Any?>): Response<ApiEnvelope<Map<String, Any?>>>

  // TODO: Replace with actual API call — GET transactions/currently-inside/
    @GET("transactions/currently-inside/")
    suspend fun currentlyInside(
        @Query("page") page: Int? = null,
        @Query("page_size") pageSize: Int? = null,
    ): Response<ApiEnvelope<Map<String, Any?>>>

  // TODO: Replace with actual API call — GET dashboard/stats/
    @GET("dashboard/stats/")
    suspend fun dashboardStats(): Response<ApiEnvelope<Map<String, Any?>>>

  // TODO: Replace with actual API call — GET trucks/?search=
    @GET("trucks/")
    suspend fun searchTrucks(
        @Query("search") search: String?,
        @Query("page") page: Int? = null,
        @Query("page_size") pageSize: Int? = null,
    ): Response<ApiEnvelope<Map<String, Any?>>>

  // TODO: Replace with actual API call — GET drivers/?search=
    @GET("drivers/")
    suspend fun searchDrivers(
        @Query("search") search: String?,
        @Query("page") page: Int? = null,
        @Query("page_size") pageSize: Int? = null,
    ): Response<ApiEnvelope<Map<String, Any?>>>
}
