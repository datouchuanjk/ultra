package io.standard.paging3.ui.refresh

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

interface RefreshComposable {
    @Composable
    fun BoxScope.Empty()

    @Composable
    fun BoxScope.FirstLoading()

    @Composable
    fun BoxScope.FirstError(refresh: () -> Unit)
}

internal val DefaultRefreshComposable = object : RefreshComposable {
    @Composable
    override fun BoxScope.Empty() {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "Empty data", fontSize = 12.sp, color = Color.Black)
                Spacer(modifier = Modifier.height(15.dp))
            }
        }
    }

    @Composable
    override fun BoxScope.FirstLoading() {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                CircularProgressIndicator(
                    strokeWidth = 1.dp,
                    color = Color.Black,
                    modifier = Modifier.size(12.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = "Loading...", fontSize = 12.sp, color = Color.Black)
            }
        }
    }

    @Composable
    override fun BoxScope.FirstError(refresh: () -> Unit) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(modifier = Modifier.clickable {
                refresh()
            }, horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "Click to Refresh", fontSize = 12.sp, color = Color.Black)
                Spacer(modifier = Modifier.height(15.dp))
                Icon(imageVector = Icons.Default.Refresh, contentDescription = null)
            }
        }
    }
}