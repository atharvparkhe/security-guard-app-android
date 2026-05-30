package com.securityguard.app.network

import com.securityguard.app.models.AuthSession
import com.securityguard.app.models.ForgotPasswordResponse
import com.securityguard.app.models.LoginResponse
import com.securityguard.app.models.OtpVerifyResponse
import com.securityguard.app.models.UserRole
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val authApi: AuthApi,
) {
    private var session: AuthSession? = null

    fun currentSession(): AuthSession? = session

    fun isLoggedIn(): Boolean = session != null

    fun logout() {
        session = null
    }

    // TODO: Replace with actual API call — POST auth/login-step-1/
    suspend fun login(employeeNumber: String, password: String): LoginResponse {
        delay(MOCK_DELAY_MS)
        return LoginResponse.mock()
    }

    // TODO: Replace with actual API call — POST auth/login-step-2/
    suspend fun verifyOtp(employeeNumber: String, otp: String): OtpVerifyResponse {
        delay(MOCK_DELAY_MS)
        val response = OtpVerifyResponse.mock()
        session = AuthSession(
            accessToken = response.accessToken,
            refreshToken = response.refreshToken,
            userName = response.userName,
            role = response.role,
        )
        return response
    }

    // TODO: Replace with actual API call — POST auth/forgot-password/send-otp/
    suspend fun forgotPasswordSendOtp(employeeNumber: String): ForgotPasswordResponse {
        delay(MOCK_DELAY_MS)
        return ForgotPasswordResponse.mock(1)
    }

    // TODO: Replace with actual API call — POST auth/forgot-password/verify-otp/
    suspend fun forgotPasswordVerifyOtp(employeeNumber: String, otp: String): ForgotPasswordResponse {
        delay(MOCK_DELAY_MS)
        return ForgotPasswordResponse.mock(2)
    }

    // TODO: Replace with actual API call — POST auth/forgot-password/set-password/
    suspend fun forgotPasswordSetPassword(
        employeeNumber: String,
        otp: String,
        newPassword: String,
    ): ForgotPasswordResponse {
        delay(MOCK_DELAY_MS)
        return ForgotPasswordResponse.mock(3)
    }

    companion object {
        private const val MOCK_DELAY_MS = 400L
    }
}
