package com.example.demo.ui

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.content.edit
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.module.basic.constant.AppConstant
import com.module.basic.route.AppRoutes
import com.module.login.ui.LoginScreen
import com.module.main.ui.MainScreen
import io.composex.nav.NavControllerLocalProvider
import org.koin.androidx.compose.get

/**
 * 全局唯一Activity  单Activity模式
 */
class HostActivity : ComponentActivity() {
    private var _navController: NavHostController? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handleIntent(intent)
        enableEdgeToEdge()
        setContent {
            val sp = get<SharedPreferences>()
            NavControllerLocalProvider { navController ->
                _navController = navController
                NavHost(
                    navController = navController,
                    startDestination = if (sp.getBoolean(AppConstant.IS_FIRST_USE, true)) {
                        sp.edit {
                            putBoolean(AppConstant.IS_FIRST_USE, false)
                        }
                        AppRoutes.Login.static
                    } else {
                        AppRoutes.Main.static
                    }
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








