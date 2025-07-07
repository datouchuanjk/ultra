package com.module.basic.ui

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.module.basic.viewmodel.BaseViewModel

/**
 * 处理viewmodel的信息
 */
@Composable
fun <T : BaseViewModel> T.withLoadingAndError(): T = apply {
    val isLoading by loadingFlow.collectAsState()
    if (isLoading) {
        Dialog(onDismissRequest = {}) {
            Box(
                modifier = Modifier.background(
                    color = Color.Black.copy(0.7f),
                    shape = RoundedCornerShape(8.dp)
                )
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(25.dp),
                    color = Color.White,
                    strokeWidth = 1.dp
                )
            }
        }
    }
    val context: Context = LocalContext.current
    LaunchedEffect(this) {
        errorFlow.collect {
            Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
        }
    }
}