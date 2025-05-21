package com.example.demo.activity

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import com.example.demo.ui.main
import com.example.demo.ui.setting
import io.datou.develop.LocalNavHostControllerProvider

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LocalNavHostControllerProvider {
                NavHost(
                    navController = this,
                    startDestination = "main"
                ) {
                    main()
                    setting()
                }
            }
        }
    }
}


