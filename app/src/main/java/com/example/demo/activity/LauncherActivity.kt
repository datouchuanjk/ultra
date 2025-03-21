package com.example.demo.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import io.datou.develop.enableFullScreen
import io.datou.develop.intentOf

class LauncherActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableFullScreen()
        setContent {
            LaunchedEffect(Unit) {
                startActivity(intentOf<MainActivity>())
                finish()
            }
        }
    }
}