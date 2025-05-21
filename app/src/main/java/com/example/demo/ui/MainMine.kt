package com.example.demo.ui

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController


fun NavGraphBuilder.mainMine() {
    composable("main_mine") {
        MainMineScreen()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainMineScreen() {
    Scaffold(topBar = {
        TopAppBar(title = {
            Text(text = "main_mine")
        })
    }) { innerPadding ->
//        val navController = LocalNavHostController.current
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Button(onClick = {
//                navController.navigate("setting")
            }) {
                Text("去设置")
            }
        }
    }
}