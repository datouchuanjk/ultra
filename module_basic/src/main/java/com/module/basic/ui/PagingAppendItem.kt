package com.module.basic.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.composex.paging.LoadState
import io.composex.paging.PagingData

fun LazyListScope.pagingAppendItem(pagingData: PagingData<*>) {
    item {
        when (val state = pagingData.appendState) {
            is LoadState.Loading -> {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CircularProgressIndicator(
                        strokeWidth = 1.dp,
                        color = Color.Black,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(15.dp))
                    Text(text = "Loading...", fontSize = 12.sp, color = Color.Black)
                }
            }

            is LoadState.Error -> {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clickable {
                            pagingData.retry()
                        },
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = null,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(15.dp))
                    Text(text = "Click to retry", fontSize = 12.sp, color = Color.Black)
                }
            }

            is LoadState.NotLoading -> {
                if (state.endOfPaginationReached) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "no more data", fontSize = 12.sp, color = Color.Black)
                    }
                }
            }
        }
    }
}