package io.composex.ui.paging

import androidx.compose.foundation.lazy.LazyListScope
import io.composex.paging.LoadState
import io.composex.paging.PagingData

fun LazyListScope.itemWithPaging(
    pagingData: PagingData<*>,
    loadingContent: (() -> Unit)? = null,
    errorContent: ((Throwable) -> Unit)? = null,
    notLoadingContent: ((Boolean) -> Unit)? = null,
) {
    item {
        when (val state = pagingData.appendState) {
            is LoadState.Loading -> {
                loadingContent?.invoke()
            }

            is LoadState.Error -> {
                errorContent?.invoke(state.error)
            }

            is LoadState.NotLoading -> {
                notLoadingContent?.invoke(state.endOfPaginationReached)
            }
        }
    }
}