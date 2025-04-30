package io.standard.paging3.ui.refresh

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T : Any> RefreshBox(
    lazyPagingItems: LazyPagingItems<T>,
    modifier: Modifier = Modifier,
    isRefreshing: Boolean = lazyPagingItems.loadState.refresh is LoadState.Loading,
    onRefresh: () -> Unit = {
        lazyPagingItems.refresh()
    },
    state: PullToRefreshState = rememberPullToRefreshState(),
    contentAlignment: Alignment = Alignment.TopStart,
    indicator: @Composable BoxScope.(Boolean) -> Unit = {
        if (it.not()) {
            Indicator(
                modifier = Modifier.align(Alignment.TopCenter),
                isRefreshing = isRefreshing,
                state = state
            )
        }
    },
    refreshComposable: RefreshComposable = DefaultRefreshComposable,
    content: @Composable BoxScope.() -> Unit
) {
    var isFirstLoad by rememberSaveable { mutableStateOf(true) }
    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
        modifier = modifier,
        state = state,
        contentAlignment = contentAlignment,
        indicator = {
            indicator(isFirstLoad)
        }
    ) {
        val refreshState by remember {
            derivedStateOf { lazyPagingItems.loadState.refresh }
        }
        if (isFirstLoad && refreshState is LoadState.Loading) {
            refreshComposable.run {
                FirstLoading()
            }
        } else if (isFirstLoad && refreshState is LoadState.Error) {
            refreshComposable.run {
                FirstError((refreshState as LoadState.Error).error) {
                    onRefresh()
                }
            }
        } else {
            if (isFirstLoad && refreshState is LoadState.NotLoading) {
                isFirstLoad = false
            }
            if (lazyPagingItems.itemCount == 0) {
                refreshComposable.run {
                    Empty {
                        onRefresh()
                    }
                }
            } else {
                content()
            }
        }
    }
}
