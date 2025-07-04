package com.module.home.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.module.home.viewmodel.HomeViewModel
import io.composex.ui.picker.DatePicker

@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel()) {
    Column(modifier = Modifier.fillMaxSize()) {
        DatePicker (
            onChange = {

            }) {
            Text(
                text = it.value.toString().padStart(2, '0'),
                fontSize = 14.sp,
                color = Color.Black
            )
        }
    }
}