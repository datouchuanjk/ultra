package com.example.demo.ui

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Call
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController


fun NavGraphBuilder.main() {
    composable("main") {
        MainScreen()
    }
}

@Composable
fun MainScreen() {
    val navController: NavHostController = rememberNavController()
    val icons = listOf(
        Icons.Rounded.Home,
        Icons.Rounded.Call,
        Icons.Rounded.Person
    )
    val labels = listOf(
        "Home",
        "Chat",
        "Mine",
    )
    val routes = listOf(
        "main_home",
        "main_chat",
        "main_mine",
    )
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    Scaffold(bottomBar = {
        NavigationBar {
            repeat(3) {
                NavigationBarItem(
                    enabled = true,
                    selected = currentBackStackEntry?.destination?.route == routes[it],
                    onClick = {
                        navController.navigate(routes[it]){
                            popUpTo(navController.graph.startDestinationId){
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    icon = { Icon(imageVector = icons[it], contentDescription = null) },
                    label = { Text(labels[it]) }
                )
            }
        }
    }) {
        NavHost(
            navController = navController,
            startDestination = "main_mine",
            modifier = Modifier.padding(it)
        ) {
            mainHome()
            mainChat()
            mainMine()
        }
    }
}