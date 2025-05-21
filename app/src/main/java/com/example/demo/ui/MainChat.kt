package com.example.demo.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController


fun NavGraphBuilder.mainChat() {
    composable("main_chat") {
        MainChatScreen()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainChatScreen() {
    Scaffold(topBar = {
        TopAppBar(title = {
            Text(text = "main_chat")
        })
    }) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Text("main_chat")
        }
    }
}