package com.securityguard.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.securityguard.app.models.ForgotPasswordResponse
import com.securityguard.app.models.LoginResponse
import com.securityguard.app.models.OtpVerifyResponse
import com.securityguard.app.network.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    val isLoggedIn: Boolean get() = authRepository.isLoggedIn()

    fun login(employeeNumber: String, password: String, onSuccess: (LoginResponse) -> Unit) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            runCatching { authRepository.login(employeeNumber, password) }
                .onSuccess(onSuccess)
                .onFailure { _error.value = it.message }
            _loading.value = false
        }
    }

    fun verifyOtp(employeeNumber: String, otp: String, onSuccess: (OtpVerifyResponse) -> Unit) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            runCatching { authRepository.verifyOtp(employeeNumber, otp) }
                .onSuccess(onSuccess)
                .onFailure { _error.value = it.message }
            _loading.value = false
        }
    }

    fun forgotSendOtp(employeeNumber: String, onSuccess: (ForgotPasswordResponse) -> Unit) {
        viewModelScope.launch {
            _loading.value = true
            runCatching { authRepository.forgotPasswordSendOtp(employeeNumber) }
                .onSuccess(onSuccess)
                .onFailure { _error.value = it.message }
            _loading.value = false
        }
    }

    fun forgotVerifyOtp(employeeNumber: String, otp: String, onSuccess: (ForgotPasswordResponse) -> Unit) {
        viewModelScope.launch {
            _loading.value = true
            runCatching { authRepository.forgotPasswordVerifyOtp(employeeNumber, otp) }
                .onSuccess(onSuccess)
                .onFailure { _error.value = it.message }
            _loading.value = false
        }
    }

    fun forgotSetPassword(
        employeeNumber: String,
        otp: String,
        newPassword: String,
        onSuccess: (ForgotPasswordResponse) -> Unit,
    ) {
        viewModelScope.launch {
            _loading.value = true
            runCatching { authRepository.forgotPasswordSetPassword(employeeNumber, otp, newPassword) }
                .onSuccess(onSuccess)
                .onFailure { _error.value = it.message }
            _loading.value = false
        }
    }

    fun logout() = authRepository.logout()
}
