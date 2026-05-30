package com.securityguard.app.models

data class LoginResponse(
    val requiresOtp: Boolean,
    val message: String,
    val maskedPhone: String? = null,
) {
    companion object {
        fun mock() = LoginResponse(
            requiresOtp = true,
            message = "OTP sent to registered mobile.",
            maskedPhone = "******3210",
        )
    }
}

data class OtpVerifyResponse(
    val accessToken: String,
    val refreshToken: String,
    val userName: String,
    val role: UserRole,
) {
    companion object {
        fun mock() = OtpVerifyResponse(
            accessToken = "mock-access-token",
            refreshToken = "mock-refresh-token",
            userName = "Guard User",
            role = UserRole.SECURITY_GUARD,
        )
    }
}

data class ForgotPasswordResponse(val message: String) {
    companion object {
        fun mock(step: Int) = ForgotPasswordResponse(
            message = when (step) {
                1 -> "OTP sent if account exists."
                2 -> "OTP verified."
                else -> "Password updated. You can sign in."
            },
        )
    }
}

data class AuthSession(
    val accessToken: String,
    val refreshToken: String,
    val userName: String,
    val role: UserRole,
)
