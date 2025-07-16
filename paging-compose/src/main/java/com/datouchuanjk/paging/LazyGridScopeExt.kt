package com.datouchuanjk.paging

import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.runtime.Composable

fun LazyGridScope.pagingItem(
    loadState: LoadState,
    key: Any? = null,
    contentType: Any? = null,
    loadingContent: @Composable LazyGridItemScope.(LoadState.Loading) -> Unit,
    notLoadingContent: @Composable LazyGridItemScope.(LoadState.NotLoading) -> Unit,
    errorContent: @Composable LazyGridItemScope.(LoadState.Error) -> Unit
) {
    item(
        span = {
            GridItemSpan(maxLineSpan)
        },
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