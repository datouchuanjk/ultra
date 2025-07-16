package com.datouchuanjk.paging

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PagingRefresh(
    modifier: Modifier = Modifier,
    pagingData: PagingData<*>,
    isRefreshing: Boolean = pagingData.refreshState is LoadState.Loading,
    onRefresh: () -> Unit = {
        pagingData.refresh()
    },
    state: PullToRefreshState = rememberPullToRefreshState(),
    contentAlignment: Alignment = Alignment.TopStart,
    indicator: @Composable BoxScope.() -> Unit = {
        Indicator(
            modifier = Modifier.align(Alignment.TopCenter),
            isRefreshing = isRefreshing,
            state = state
        )
    },
    skeletonContent: @Composable (() -> Unit)? = null,
    errorContent: @Composable (() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit
) {
    if (pagingData.isFirstRefresh && pagingData.refreshState !is LoadState.Error && skeletonContent != null) {
        skeletonContent()
    } else if (pagingData.isFirstRefresh && pagingData.refreshState is LoadState.Error && errorContent != null) {
        errorContent()
    } else {
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = onRefresh,
            modifier = modifier,
            state = state,
            contentAlignment = contentAlignment,
            indicator = indicator,
            content = content
        )
    }
}