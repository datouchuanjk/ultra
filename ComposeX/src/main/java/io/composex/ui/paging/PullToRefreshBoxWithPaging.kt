package io.composex.ui.paging

import android.util.Log
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import io.composex.paging.LoadState
import io.composex.paging.PagingData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PullToRefreshBoxWithPaging(
    pagingData: PagingData<*>,
    state: PullToRefreshState = rememberPullToRefreshState(),
    skeletonContent: (@Composable () -> Unit)? = null,
    errorContent: (@Composable () -> Unit)? = null,
    emptyContent: (@Composable () -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit
) {
    val isRefreshing by remember(pagingData) {
        derivedStateOf {
            if (pagingData.isFirstRefresh) {
                when (pagingData.refreshState) {
                    is LoadState.Loading,
                    is LoadState.NotLoading -> {
                        skeletonContent == null
                    }

                    is LoadState.Error -> {
                        errorContent == null
                    }
                }
            } else {
                pagingData.refreshState is LoadState.Loading
            }
        }
    }
    Log.e("1234", "isRefreshing=$isRefreshing")
    PullToRefreshBox(
        isRefreshing = isRefreshing,
        state = state,
        onRefresh = {
            pagingData.refresh()
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