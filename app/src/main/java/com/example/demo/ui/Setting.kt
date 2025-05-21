package com.example.demo.ui

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable


fun NavGraphBuilder.setting() {
    composable("setting") {
        SettingScreen()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen() {
   val a = LocalOnBackPressedDispatcherOwner.current
    Scaffold(topBar = {
        TopAppBar(title = {
            Text(text = "Setting")
        }, navigationIcon = {
            IconButton(onClick = {
                 a?.onBackPressedDispatcher?.onBackPressed()
            }) {
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
            }
        })
    }) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Text("我是设置界面")
        }
    }
}