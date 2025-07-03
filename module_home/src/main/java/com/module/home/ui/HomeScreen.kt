package com.module.home.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.module.basic.route.Routes
import com.module.home.viewmodel.HomeViewModel
import io.composex.nav.LocalNavController

@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel()) {
    Column(modifier = Modifier.fillMaxSize()) {
        val localNavController = LocalNavController.current
        Button(onClick = {
            localNavController.navigate(
                Routes.Login.dynamic(
                  "username" to "username",
                  "password" to "password"
                )
            )
        }) {
            Text("go Login")
        }
    }
}