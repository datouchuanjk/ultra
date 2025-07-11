package com.example.demo.ui

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.core.content.edit
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import io.watermelon.banner.Banner
import io.watermelon.nav.NavControllerLocalProvider
import io.watermelon.nav.navigateForResult
import io.watermelon.wheel.WheelDefaults

/**
 * 全局唯一Activity  单Activity模式
 */
class HostActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handleIntent(intent)
        enableEdgeToEdge()
        setContent {
            Text("123")
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








