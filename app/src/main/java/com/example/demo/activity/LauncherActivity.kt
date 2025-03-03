package com.example.demo.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import io.datou.chat.helper.ChatHelper
import io.datou.develop.enableFullScreen
import io.datou.develop.intentOf

class LauncherActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableFullScreen()
        setContent {
            LaunchedEffect(Unit) {
                ChatHelper.init("1199221128116146#demo")
                ChatHelper.login("user1", "123456")
                startActivity(intentOf<MainActivity>())
                finish()
            }
        }
    }
}