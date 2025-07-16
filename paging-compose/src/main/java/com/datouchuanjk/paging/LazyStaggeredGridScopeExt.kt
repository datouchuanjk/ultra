package com.datouchuanjk.paging

import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridItemScope
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridScope
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.runtime.Composable

fun LazyStaggeredGridScope.pagingItem(
    loadState: LoadState,
    key: Any? = null,
    contentType: Any? = null,
    loadingContent: @Composable LazyStaggeredGridItemScope.(LoadState.Loading) -> Unit,
    notLoadingContent: @Composable LazyStaggeredGridItemScope.(LoadState.NotLoading) -> Unit,
    errorContent: @Composable LazyStaggeredGridItemScope.(LoadState.Error) -> Unit
) {
    item(
        span = StaggeredGridItemSpan.SingleLane,
        key = key,
        contentType = contentType,
    ) {
        when (loadState) {
            is LoadState.Loading -> {
                loadingContent(loadState)
            }

            is LoadState.Error -> {
                errorContent(loadState)
            }

            is LoadState.NotLoading -> {
                notLoadingContent(loadState)
            }
        }
    }
}