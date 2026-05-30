package com.securityguard.app.network

import com.google.gson.annotations.SerializedName

data class ApiEnvelope<T>(
    @SerializedName("response_type") val responseType: String?,
    val message: String?,
    val content: T?,
) {
    val isSuccess: Boolean
        get() = responseType?.equals("ERROR", ignoreCase = true) != true
}

data class LoginStep1Request(
    @SerializedName("employee_number") val employeeNumber: String,
    val password: String,
)

data class LoginStep2Request(
    @SerializedName("employee_number") val employeeNumber: String,
    val otp: String,
)

data class ForgotPasswordSendOtpRequest(
    @SerializedName("employee_number") val employeeNumber: String,
)

data class ForgotPasswordVerifyOtpRequest(
    @SerializedName("employee_number") val employeeNumber: String,
    val otp: String,
)

data class ForgotPasswordSetPasswordRequest(
    @SerializedName("employee_number") val employeeNumber: String,
    val otp: String,
    @SerializedName("new_password") val newPassword: String,
)

data class AllowInRequest(
    @SerializedName("in_time") val inTime: String? = null,
)

data class MarkExitRequest(
    @SerializedName("out_time") val outTime: String? = null,
)
