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
import com.module.basic.route.AppRoutes
import com.module.login.ui.LoginScreen
import com.module.main.ui.MainScreen
import dagger.hilt.android.AndroidEntryPoint
import io.composex.nav.NavControllerLocalProvider
import io.composex.util.installApk
import io.composex.util.mimeType

/**
 * 全局唯一Activity  单Activity模式
 */
@AndroidEntryPoint
class HostActivity : ComponentActivity() {
    private var _navController: NavHostController? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val a = "aaa.apk"
        Log.d("HostActivity","a=${a.mimeType}")
        installApk()
        handleIntent(intent)
        enableEdgeToEdge()
        setContent {
            NavControllerLocalProvider { navController ->
                _navController = navController
                NavHost(
                    navController = navController,
                    startDestination = AppRoutes.Main.static
                ) {
                    composable(route = AppRoutes.Main.static) {
                        MainScreen()
                    }
                    composable(route = AppRoutes.Login.static) {
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

    /**
     * 此方法用于处理 推送 比如说：点击推送进入app or 唤醒
     */
    private fun handleIntent(intent: Intent) {

    }
}








