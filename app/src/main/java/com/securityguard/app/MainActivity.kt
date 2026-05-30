package com.securityguard.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.securityguard.app.ui.navigation.AppNavGraph
import com.securityguard.app.ui.theme.AppColors
import com.securityguard.app.ui.theme.SecurityGuardTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SecurityGuardTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = AppColors.GateBackground,
                ) {
                    AppNavGraph()
                }
            }
        }
    }
}
