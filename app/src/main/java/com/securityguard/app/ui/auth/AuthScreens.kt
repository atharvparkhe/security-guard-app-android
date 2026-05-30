package com.securityguard.app.ui.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.hilt.navigation.compose.hiltViewModel
import com.securityguard.app.ui.components.OtpInputRow
import com.securityguard.app.ui.theme.AppSpacing
import com.securityguard.app.ui.theme.AppTheme
import com.securityguard.app.ui.theme.AppTypography
import com.securityguard.app.ui.theme.LocalPortalTheme
import com.securityguard.app.ui.theme.SecurityGuardTheme
import com.securityguard.app.viewmodel.AuthViewModel

@Composable
fun LoginScreen(
    onOtpRequired: (String) -> Unit,
    onForgotPassword: () -> Unit,
    onLoggedIn: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel(),
) {
    var employeeNumber by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isSecurityPortal by remember { mutableStateOf(true) }
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()
    val portal = if (isSecurityPortal) AppTheme.Security else AppTheme.Stores

    SecurityGuardTheme(portalTheme = portal) {
        val theme = LocalPortalTheme.current
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(AppSpacing.BaseMargin),
            verticalArrangement = Arrangement.Center,
        ) {
            Text("Gate Control", style = AppTypography.Display)
            Text("Sign in to continue", style = AppTypography.Subheadline)
            Spacer(Modifier.height(AppSpacing.Section))

            SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                SegmentedButton(
                    selected = isSecurityPortal,
                    onClick = { isSecurityPortal = true },
                    shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2),
                ) { Text("Security") }
                SegmentedButton(
                    selected = !isSecurityPortal,
                    onClick = { isSecurityPortal = false },
                    shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2),
                ) { Text("Stores") }
            }
            Spacer(Modifier.height(AppSpacing.Standard))

            OutlinedTextField(
                value = employeeNumber,
                onValueChange = { employeeNumber = it.filter(Char::isDigit) },
                label = { Text("Employee number") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
            )
            Spacer(Modifier.height(AppSpacing.Close))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true,
            )
            error?.let {
                Spacer(Modifier.height(AppSpacing.Tight))
                Text(it, style = AppTypography.Footnote, color = theme.primary)
            }
            Spacer(Modifier.height(AppSpacing.Section))
            Button(
                onClick = {
                    viewModel.login(employeeNumber, password) { response ->
                        if (response.requiresOtp) onOtpRequired(employeeNumber) else onLoggedIn()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !loading,
            ) {
                if (loading) CircularProgressIndicator() else Text("Continue")
            }
            TextButton(onClick = onForgotPassword) { Text("Forgot password?") }
        }
    }
}

@Composable
fun OtpScreen(
    employeeNumber: String,
    onVerified: () -> Unit,
    onBack: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel(),
) {
    var otp by remember { mutableStateOf("") }
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()

    SecurityGuardTheme {
        Column(
            Modifier
                .fillMaxSize()
                .padding(AppSpacing.BaseMargin),
            verticalArrangement = Arrangement.Center,
        ) {
            Text("Verify OTP", style = AppTypography.Title1)
            Text("Sent to registered mobile for $employeeNumber", style = AppTypography.Subheadline)
            Spacer(Modifier.height(AppSpacing.Section))
            OtpInputRow(otp)
            Spacer(Modifier.height(AppSpacing.Standard))
            OutlinedTextField(
                value = otp,
                onValueChange = { if (it.length <= 6 && it.all(Char::isDigit)) otp = it },
                label = { Text("6-digit OTP") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
            )
            error?.let { Text(it, style = AppTypography.Footnote) }
            Spacer(Modifier.height(AppSpacing.Section))
            Button(
                onClick = {
                    viewModel.verifyOtp(employeeNumber, otp) { onVerified() }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = otp.length == 6 && !loading,
            ) {
                if (loading) CircularProgressIndicator() else Text("Verify & sign in")
            }
            TextButton(onClick = onBack) { Text("Back") }
        }
    }
}

@Composable
fun ForgotPasswordStep1Screen(
    onOtpSent: (String) -> Unit,
    onBack: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel(),
) {
    var employeeNumber by remember { mutableStateOf("") }
    val loading by viewModel.loading.collectAsState()

    ForgotPasswordScaffold(step = 1, title = "Forgot password") {
        OutlinedTextField(
            value = employeeNumber,
            onValueChange = { employeeNumber = it.filter(Char::isDigit) },
            label = { Text("Employee number") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        )
        Spacer(Modifier.height(AppSpacing.Section))
        Button(
            onClick = { viewModel.forgotSendOtp(employeeNumber) { onOtpSent(employeeNumber) } },
            modifier = Modifier.fillMaxWidth(),
            enabled = employeeNumber.isNotBlank() && !loading,
        ) { Text("Send OTP") }
        TextButton(onClick = onBack) { Text("Back to login") }
    }
}

@Composable
fun ForgotPasswordStep2Screen(
    employeeNumber: String,
    onVerified: (String) -> Unit,
    viewModel: AuthViewModel = hiltViewModel(),
) {
    var otp by remember { mutableStateOf("") }
    val loading by viewModel.loading.collectAsState()

    ForgotPasswordScaffold(step = 2, title = "Verify OTP") {
        OtpInputRow(otp)
        Spacer(Modifier.height(AppSpacing.Standard))
        OutlinedTextField(
            value = otp,
            onValueChange = {
                val digitsOnly = it.filter(Char::isDigit)
                if (digitsOnly.length <= 6) otp = digitsOnly
            },
            label = { Text("OTP") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
        )
        Spacer(Modifier.height(AppSpacing.Section))
        Button(
            onClick = { viewModel.forgotVerifyOtp(employeeNumber, otp) { onVerified(otp) } },
            modifier = Modifier.fillMaxWidth(),
            enabled = otp.length == 6 && !loading,
        ) { Text("Verify OTP") }
    }
}

@Composable
fun ForgotPasswordStep3Screen(
    employeeNumber: String,
    otp: String,
    onComplete: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel(),
) {
    var password by remember { mutableStateOf("") }
    var confirm by remember { mutableStateOf("") }
    val loading by viewModel.loading.collectAsState()

    ForgotPasswordScaffold(step = 3, title = "Set new password") {
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("New password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
        )
        Spacer(Modifier.height(AppSpacing.Close))
        OutlinedTextField(
            value = confirm,
            onValueChange = { confirm = it },
            label = { Text("Confirm password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
        )
        Spacer(Modifier.height(AppSpacing.Section))
        Button(
            onClick = {
                viewModel.forgotSetPassword(employeeNumber, otp, password) { onComplete() }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = password == confirm && password.length >= 6 && !loading,
        ) { Text("Update password") }
    }
}

@Composable
private fun ForgotPasswordScaffold(
    step: Int,
    title: String,
    content: @Composable ColumnScope.() -> Unit,
) {
    SecurityGuardTheme {
        Column(
            Modifier
                .fillMaxSize()
                .padding(AppSpacing.BaseMargin),
            horizontalAlignment = Alignment.Start,
        ) {
            Text("Step $step of 3", style = AppTypography.Caption)
            Text(title, style = AppTypography.Title1)
            Spacer(Modifier.height(AppSpacing.Section))
            content()
        }
    }
}
