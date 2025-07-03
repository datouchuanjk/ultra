package com.module.mine.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.module.basic.route.Routes
import com.module.mine.viewmodel.MineViewModel
import io.composex.nav.LocalNavController

@Composable
fun MineScreen(viewModel: MineViewModel = hiltViewModel()) {
    Column {
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

        var input by rememberSaveable {
            mutableStateOf("")
        }
        Button(onClick = {
            input = System.currentTimeMillis().toString()
        }) {
            Text("input=$input")
        }
    }
}