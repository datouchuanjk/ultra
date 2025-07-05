package com.example.demo.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.module.basic.route.Routes
import com.module.login.ui.LoginScreen
import com.module.main.ui.MainScreen
import dagger.hilt.android.AndroidEntryPoint
import io.composex.nav.NavControllerLocalProvider

@AndroidEntryPoint
class HostActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NavControllerLocalProvider {
                NavHost(
                    navController = it,
                    startDestination = Routes.Main.static
                ) {
                    composable(route = Routes.Login.static) {
                        LoginScreen()
                    }
                    composable(route = Routes.Main.static) {
                        MainScreen()
                    }
                }
            }
        }
    }
}






