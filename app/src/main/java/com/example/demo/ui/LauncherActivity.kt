package com.example.demo.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import io.datou.develop.addSecureFlag
import io.datou.develop.enableFullScreen
import io.datou.develop.intentOf
import kotlinx.coroutines.delay

class LauncherActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableFullScreen()
        addSecureFlag()
        setContent {
            LaunchedEffect(Unit) {
                delay(500)
                startActivity(intentOf<MainActivity>())
            }
        }
    }
}