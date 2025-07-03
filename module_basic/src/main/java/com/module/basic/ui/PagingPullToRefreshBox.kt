package com.module.basic.ui

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.composex.paging.LoadState
import io.composex.paging.PagingData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PagingPullToRefreshBox(
    pagingData: PagingData<*>,
    state: PullToRefreshState = rememberPullToRefreshState(),
    skeletonContent: (@Composable () -> Unit)? = null,
    errorContent: (@Composable () -> Unit)? = null,
    emptyContent: (@Composable () -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit
) {
    PullToRefreshBox(
        isRefreshing = !pagingData.isFirstRefresh && pagingData.refreshState is LoadState.Loading,
        state = state,
        onRefresh = {
            if (!pagingData.isFirstRefresh) {
                pagingData.refresh()
            }
        }, indicator = {
            if(!pagingData.isFirstRefresh){
                Indicator(
                    modifier = Modifier.align(Alignment.TopCenter),
                    isRefreshing = !pagingData.isFirstRefresh && pagingData.refreshState is LoadState.Loading,
                    state = state
                )
            }
        }
    ) {
        if (skeletonContent != null && pagingData.isFirstRefresh
            && (pagingData.refreshState is LoadState.NotLoading
                    || pagingData.refreshState is LoadState.Loading)
        ) {
            skeletonContent()
        } else if (errorContent != null && pagingData.isFirstRefresh && pagingData.refreshState is LoadState.Error) {
            errorContent()
        } else if (emptyContent != null && pagingData.count == 0) {
            emptyContent()
        } else {
            content()
        }
    }
}