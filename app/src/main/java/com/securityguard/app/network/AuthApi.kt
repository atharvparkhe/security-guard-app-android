package com.securityguard.app.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
  // TODO: Replace with actual API call — POST auth/login-step-1/
    @POST("auth/login-step-1/")
    suspend fun loginStep1(@Body body: LoginStep1Request): Response<ApiEnvelope<Map<String, Any?>>>

  // TODO: Replace with actual API call — POST auth/login-step-2/
    @POST("auth/login-step-2/")
    suspend fun loginStep2(@Body body: LoginStep2Request): Response<ApiEnvelope<Map<String, Any?>>>

  // TODO: Replace with actual API call — POST auth/token/refresh/
    @POST("auth/token/refresh/")
    suspend fun refreshToken(@Body body: Map<String, String>): Response<ApiEnvelope<Map<String, Any?>>>

  // TODO: Replace with actual API call — POST auth/forgot-password/send-otp/
    @POST("auth/forgot-password/send-otp/")
    suspend fun forgotPasswordSendOtp(@Body body: ForgotPasswordSendOtpRequest): Response<ApiEnvelope<Unit>>

  // TODO: Replace with actual API call — POST auth/forgot-password/verify-otp/
    @POST("auth/forgot-password/verify-otp/")
    suspend fun forgotPasswordVerifyOtp(@Body body: ForgotPasswordVerifyOtpRequest): Response<ApiEnvelope<Unit>>

  // TODO: Replace with actual API call — POST auth/forgot-password/set-password/
    @POST("auth/forgot-password/set-password/")
    suspend fun forgotPasswordSetPassword(@Body body: ForgotPasswordSetPasswordRequest): Response<ApiEnvelope<Unit>>
}
