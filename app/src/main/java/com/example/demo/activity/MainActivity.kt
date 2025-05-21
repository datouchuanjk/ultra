package com.example.demo.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.window.Dialog
import androidx.navigation.compose.NavHost
import com.example.demo.ui.main
import com.example.demo.ui.setting
import io.datou.develop.LocalNavHostControllerProvider
import io.datou.develop.findDialogWindow

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Dialog({}) {
                findDialogWindow()
            }
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


