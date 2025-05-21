package com.example.demo.ui

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainHomeScreen(viewModel: MainHomeViewModel =viewModel()) {
    Scaffold(topBar = {
        TopAppBar(title = {
            Text(text = "main_home")
        })
    }) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Text("main_home")
            TextField(value = viewModel.input, onValueChange = { viewModel.input = it })
            LaunchedEffect(viewModel) {
                Log.e("1234","巴疼")
            }
        }
    }
}

class MainHomeViewModel : ViewModel() {

    var input by mutableStateOf("")

    override fun onCleared() {
        super.onCleared()
    }
}