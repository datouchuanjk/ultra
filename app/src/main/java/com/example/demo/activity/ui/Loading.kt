package com.example.demo.activity.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.demo.viewmodel.BaseViewModel
import io.datou.develop.toast
import kotlinx.coroutines.flow.collectLatest

@Composable
fun HandleViewModel(viewModel: BaseViewModel) {
    LaunchedEffect(Unit) {
        viewModel.error.collectLatest {
            toast(it.message)
        }
    }
    val loading by viewModel.loading.collectAsState()
    val isLoading by remember {
        derivedStateOf {
            loading > 0
        }
    }
    if (isLoading) {
        Dialog(onDismissRequest = {}) {
            Box(
                modifier = Modifier
                    .background(color = Color.Black, shape = RoundedCornerShape(8.dp))
                    .padding(15.dp)
            ) {
                CircularProgressIndicator(color = Color.White, strokeWidth = 1.dp)
            }
        }
    }
}