package io.standard.paging3.ui.refresh

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T : Any> RefreshBox(
    lazyPagingItems: LazyPagingItems<T>,
    isRefreshing: Boolean = lazyPagingItems.loadState.refresh is LoadState.Loading,
    onRefresh: () -> Unit = {
        lazyPagingItems.refresh()
    },
    modifier: Modifier = Modifier,
    state: PullToRefreshState = rememberPullToRefreshState(),
    contentAlignment: Alignment = Alignment.TopStart,
    indicator: @Composable BoxScope.(Boolean) -> Unit = {
        Indicator(
            modifier = Modifier.align(Alignment.TopCenter),
            isRefreshing = isRefreshing,
            state = state
        )
    },
    refreshComposable: RefreshComposable = DefaultRefreshComposable,
    content: @Composable BoxScope.() -> Unit
) {
    var isFirstLoad by remember { mutableStateOf(true) }
    PullToRefreshBox(
        isRefreshing = isRefreshing&&!isFirstLoad,
        onRefresh = onRefresh,
        modifier = modifier,
        state = state,
        contentAlignment = contentAlignment,
        indicator = {
            indicator(isRefreshing&&!isFirstLoad)
        }
    ) {
        if (isFirstLoad && lazyPagingItems.loadState.refresh is LoadState.Loading) {
            refreshComposable.run {
                FirstLoading()
            }
        } else if (isFirstLoad && lazyPagingItems.loadState.refresh is LoadState.Error) {
            refreshComposable.run {
                FirstError {
                    lazyPagingItems.refresh()
                    onRefresh()
                }
            }
        } else {
            if (isFirstLoad && lazyPagingItems.loadState.refresh is LoadState.NotLoading) {
                isFirstLoad = false
            }
            if (lazyPagingItems.itemCount == 0) {
                refreshComposable.run {
                    Empty()
                }
            } else {
                content()
            }
        }
    }
}
