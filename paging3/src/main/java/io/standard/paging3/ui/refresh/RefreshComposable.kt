package io.standard.paging3.ui.refresh

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

interface RefreshComposable {
    @Composable
    fun BoxScope.Empty(refresh: () -> Unit)

    @Composable
    fun BoxScope.FirstLoading()

    @Composable
    fun BoxScope.FirstError(throwable: Throwable, refresh: () -> Unit)
}

internal val DefaultRefreshComposable = object : RefreshComposable {
    @Composable
    override fun BoxScope.Empty(refresh: () -> Unit) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(
                modifier = Modifier.clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                ) {
                    refresh()
                }, horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = null,
                    modifier = Modifier.size(30.dp)
                )
                Spacer(modifier = Modifier.height(30.dp))
                Text(
                    text = "Empty Data Click to Refresh",
                    fontSize = 14.sp,
                    color = Color.Black
                )
            }
        }
    }

    @Composable
    override fun BoxScope.FirstLoading() {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 15.dp), contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                CircularProgressIndicator(
                    strokeWidth = 2.dp,
                    color = Color.Black,
                )
            }
        }
    }

    @Composable
    override fun BoxScope.FirstError(throwable: Throwable, refresh: () -> Unit) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 15.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                ) {
                    refresh()
                }, horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = null,
                    modifier = Modifier.size(30.dp)
                )
                Spacer(modifier = Modifier.height(30.dp))
                Text(text = throwable.message?:"Nothing error", fontSize = 14.sp, color = Color.Black)
                Spacer(modifier = Modifier.height(15.dp))
                Text(text = "Load Error Click to Refresh", fontSize = 14.sp, color = Color.Black)
            }
        }
    }
}