package io.standard.paging3.ui.load

import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey


fun <T : Any> LazyListScope.items(
    lazyPagingItems: LazyPagingItems<T>,
    key: ((item: @JvmSuppressWildcards T) -> Any)? = null,
    contentType: (index: Int) -> Any? = { null },
    loadMoreComposable: LoadMoreComposable = DefaultLoadMoreComposable,
    itemContent: @Composable LazyItemScope.(item: T?) -> Unit
) {
    item(key = "lazyPagingItemsAppendItems") {
        HandlePrepend(
            lazyPagingItems = lazyPagingItems,
            loadMoreComposable = loadMoreComposable
        )
    }
    items(
        count = lazyPagingItems.itemCount,
        key = lazyPagingItems.itemKey(key),
        contentType = contentType,
    ) { index ->
        val item = lazyPagingItems[index]
        itemContent(item)
    }
    item(key = "lazyPagingItemsPrependItems") {
        HandleAppend(
            lazyPagingItems = lazyPagingItems,
            loadMoreComposable = loadMoreComposable
        )
    }
}

@Composable
internal fun <T : Any> LazyItemScope.HandlePrepend(
    lazyPagingItems: LazyPagingItems<T>,
    loadMoreComposable: LoadMoreComposable
) {
    when (val state = lazyPagingItems.loadState.prepend) {
        is LoadState.Loading -> {
            loadMoreComposable.run { PrependLoading() }
        }

        is LoadState.NotLoading -> {
            loadMoreComposable.run {
                PrependNotLoading(state.endOfPaginationReached)
            }
        }

        is LoadState.Error -> {
            loadMoreComposable.run {
                PrependError(state.error) {
                    lazyPagingItems.retry()
                }
            }
        }
    }
}

@Composable
internal fun <T : Any> LazyItemScope.HandleAppend(
    lazyPagingItems: LazyPagingItems<T>,
    loadMoreComposable: LoadMoreComposable
) {
    when (val state = lazyPagingItems.loadState.append) {
        is LoadState.Loading -> {
            loadMoreComposable.run {
                AppendLoading()
            }
        }

        is LoadState.NotLoading -> {
            loadMoreComposable.run {
                AppendNotLoading(state.endOfPaginationReached)
            }
        }

        is LoadState.Error -> {
            loadMoreComposable.run {
                AppendError(state.error) {
                    lazyPagingItems.retry()
                }
            }
        }
    }
}
