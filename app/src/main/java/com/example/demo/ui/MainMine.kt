package com.example.demo.ui

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import io.datou.develop.LocalNavHostController



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainMineScreen(viewModel:MainMineViewModel=viewModel()) {
    Scaffold(topBar = {
        TopAppBar(title = {
            Text(text = "main_mine")
        })
    }) { innerPadding ->
        val navController = LocalNavHostController.current
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Button(onClick = {
                navController?.navigate("setting")
            }) {
                Text("去设置")
                TextField(value = viewModel.input, onValueChange = { viewModel.input = it })
            }
        }
    }
}

class  MainMineViewModel: ViewModel(){
    var input by mutableStateOf("")
}