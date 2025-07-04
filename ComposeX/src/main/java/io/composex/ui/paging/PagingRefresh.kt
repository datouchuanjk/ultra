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
fun PagingRefresh(
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
                        if(skeletonContent == null){
                            pagingData.refreshState is LoadState.Loading
                        }else{
                            false
                        }
                    }

                    is LoadState.Error -> {
                        if(errorContent == null){
                            pagingData.refreshState is LoadState.Loading
                        }else{
                            false
                        }
                    }
                }
            } else {
                pagingData.refreshState is LoadState.Loading
            }
        }
    }
    Log.e("1234", "isRefreshing=$isRefreshing")
    Log.e("1234", "isRefreshing=${pagingData.refreshState is LoadState.Loading}")
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