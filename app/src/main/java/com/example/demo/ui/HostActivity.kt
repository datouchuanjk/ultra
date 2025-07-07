package com.example.demo.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.module.basic.route.Routes
import com.module.login.ui.LoginScreen
import com.module.main.ui.MainScreen
import dagger.hilt.android.AndroidEntryPoint
import io.composex.nav.NavControllerLocalProvider

/**
 * 全局唯一Activity  单Activity模式
 */
@AndroidEntryPoint
class HostActivity : ComponentActivity() {
    private var _navController: NavHostController? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handleIntent(intent)
        enableEdgeToEdge()
        setContent {
            NavControllerLocalProvider { navController ->
                _navController = navController
                NavHost(
                    navController = navController,
                    startDestination = Routes.Main.static
                ) {
                    composable(route = Routes.Main.static) {
                        MainScreen()
                    }
                    composable(route = Routes.Login.static) {
                        LoginScreen()
                    }
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private  fun handleIntent(intent: Intent){

    }
}








