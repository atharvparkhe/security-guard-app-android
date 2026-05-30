package com.securityguard.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.securityguard.app.ui.auth.ForgotPasswordStep1Screen
import com.securityguard.app.ui.auth.ForgotPasswordStep2Screen
import com.securityguard.app.ui.auth.ForgotPasswordStep3Screen
import com.securityguard.app.ui.auth.LoginScreen
import com.securityguard.app.ui.auth.OtpScreen
import com.securityguard.app.ui.guard.GuardHomeScreen
import com.securityguard.app.ui.guard.InwardDetailScreen
import com.securityguard.app.ui.guard.NewEntryPlaceholderScreen
import com.securityguard.app.ui.guard.OutwardDetailScreen
import com.securityguard.app.ui.guard.VisitorDetailScreen
import com.securityguard.app.viewmodel.AuthViewModel

@Composable
fun AppNavGraph(
    authViewModel: AuthViewModel = hiltViewModel(),
) {
    val navController = rememberNavController()
    val startDestination = remember(authViewModel.isLoggedIn) {
        if (authViewModel.isLoggedIn) Routes.GUARD_HOME else Routes.LOGIN
    }

    NavHost(navController = navController, startDestination = startDestination) {
        composable(Routes.LOGIN) {
            LoginScreen(
                onOtpRequired = { emp -> navController.navigate(Routes.otp(emp)) },
                onForgotPassword = { navController.navigate(Routes.FORGOT_STEP1) },
                onLoggedIn = {
                    navController.navigate(Routes.GUARD_HOME) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
            )
        }
        composable(
            route = Routes.OTP,
            arguments = listOf(navArgument("employeeNumber") { type = NavType.StringType }),
        ) { backStack ->
            val employeeNumber = backStack.arguments?.getString("employeeNumber").orEmpty()
            OtpScreen(
                employeeNumber = employeeNumber,
                onVerified = {
                    navController.navigate(Routes.GUARD_HOME) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                onBack = { navController.popBackStack() },
            )
        }
        composable(Routes.FORGOT_STEP1) {
            ForgotPasswordStep1Screen(
                onOtpSent = { emp -> navController.navigate(Routes.forgotStep2(emp)) },
                onBack = { navController.popBackStack() },
            )
        }
        composable(
            route = "forgot/step2/{employeeNumber}",
            arguments = listOf(navArgument("employeeNumber") { type = NavType.StringType }),
        ) { backStack ->
            val employeeNumber = backStack.arguments?.getString("employeeNumber").orEmpty()
            ForgotPasswordStep2Screen(
                employeeNumber = employeeNumber,
                onVerified = { otp -> navController.navigate(Routes.forgotStep3(employeeNumber, otp)) },
            )
        }
        composable(
            route = "forgot/step3/{employeeNumber}/{otp}",
            arguments = listOf(
                navArgument("employeeNumber") { type = NavType.StringType },
                navArgument("otp") { type = NavType.StringType },
            ),
        ) { backStack ->
            val employeeNumber = backStack.arguments?.getString("employeeNumber").orEmpty()
            val otp = backStack.arguments?.getString("otp").orEmpty()
            ForgotPasswordStep3Screen(
                employeeNumber = employeeNumber,
                otp = otp,
                onComplete = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
            )
        }
        composable(Routes.GUARD_HOME) {
            GuardHomeScreen(
                onLogout = {
                    authViewModel.logout()
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.GUARD_HOME) { inclusive = true }
                    }
                },
                onInwardDetail = { id -> navController.navigate(Routes.inwardDetail(id)) },
                onOutwardDetail = { id -> navController.navigate(Routes.outwardDetail(id)) },
                onVisitorDetail = { id -> navController.navigate(Routes.visitorDetail(id)) },
                onNewEntry = { navController.navigate("new_entry") },
            )
        }
        composable("new_entry") {
            NewEntryPlaceholderScreen(onBack = { navController.popBackStack() })
        }
        composable(
            route = Routes.INWARD_DETAIL,
            arguments = listOf(navArgument("entryId") { type = NavType.StringType }),
        ) { backStack ->
            val id = backStack.arguments?.getString("entryId").orEmpty()
            InwardDetailScreen(entryId = id, onBack = { navController.popBackStack() })
        }
        composable(
            route = Routes.OUTWARD_DETAIL,
            arguments = listOf(navArgument("entryId") { type = NavType.StringType }),
        ) { backStack ->
            val id = backStack.arguments?.getString("entryId").orEmpty()
            OutwardDetailScreen(entryId = id, onBack = { navController.popBackStack() })
        }
        composable(
            route = Routes.VISITOR_DETAIL,
            arguments = listOf(navArgument("entryId") { type = NavType.StringType }),
        ) { backStack ->
            val id = backStack.arguments?.getString("entryId").orEmpty()
            VisitorDetailScreen(entryId = id, onBack = { navController.popBackStack() })
        }
    }
}
