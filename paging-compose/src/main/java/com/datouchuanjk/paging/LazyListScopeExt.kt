package com.datouchuanjk.paging

import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable

fun LazyListScope.pagingItem(
    loadState: LoadState,
    key: Any? = null,
    contentType: Any? = null,
    loadingContent: @Composable LazyItemScope.(LoadState.Loading) -> Unit,
    notLoadingContent: @Composable LazyItemScope.(LoadState.NotLoading) -> Unit,
    errorContent: @Composable LazyItemScope.(LoadState.Error) -> Unit
) {
    item(
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