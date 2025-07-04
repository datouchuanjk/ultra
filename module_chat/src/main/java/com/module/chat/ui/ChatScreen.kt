package com.module.chat.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.util.UnstableApi
import com.module.basic.ui.AppTopBar
import com.module.chat.viewmodel.ChatViewModel
import io.composex.ui.paging.PagingRefresh
import io.composex.ui.paging.pagingItem

@androidx.annotation.OptIn(UnstableApi::class)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(viewModel: ChatViewModel = hiltViewModel()) {
    Column(
        modifier = Modifier.background(color = Color(0xfff5f5f5)),
    ) {
        AppTopBar("Chat", false)
        val pagingData by viewModel.paging.flow.collectAsState()
        PagingRefresh(
            pagingData = pagingData,
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(15.dp),
                verticalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                items(
                    count = pagingData.count,
                    key = pagingData.itemKey { it }
                ) {
                    val item = pagingData[it]
                    Text(
                        modifier = Modifier
                            .fillParentMaxWidth()
                            .height(50.dp)
                            .background(color = Color.White, shape = RoundedCornerShape(8.dp))
                            .wrapContentSize(),
                        text = item
                    )
                }
                pagingItem(pagingData)
            }
        }
    }
}