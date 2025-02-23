package io.standard.paging3.ui.load

import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey

internal enum class LoadItemType {
    Prepend, Append
}

data class LoadItems<T>(
    val item: T,
    val index: Int,
    val lastIndex: Int,
)

fun <T : Any> LazyListScope.items(
    lazyPagingItems: LazyPagingItems<T>,
    key: ((item: T) -> Any)? = null,
    contentType: (index: Int) -> Any? = { null },
    loadComposable: LoadComposable = DefaultLoadComposable,
    itemContent: @Composable LazyItemScope.(item: LoadItems<T>) -> Unit
) {
    item(key = LoadItemType.Prepend) {
        HandlePrepend(
            lazyPagingItems = lazyPagingItems,
            loadComposable = loadComposable
        )
    }
    items(
        count = lazyPagingItems.itemCount,
        key = lazyPagingItems.itemKey(key),
        contentType = contentType,
    ) { index ->
        val item = lazyPagingItems[index]
        if (item == null) {
            loadComposable.run {
                Placeholders()
            }
        } else {
            itemContent(
                LoadItems(
                    item = item,
                    index = index,
                    lastIndex = lazyPagingItems.itemCount - 1
                )
            )
        }
    }
    item(key = LoadItemType.Append) {
        HandleAppend(
            lazyPagingItems = lazyPagingItems,
            loadComposable = loadComposable
        )
    }
}

@Composable
internal fun <T : Any> LazyItemScope.HandlePrepend(
    lazyPagingItems: LazyPagingItems<T>,
    loadComposable: LoadComposable
) {
    when (val state = lazyPagingItems.loadState.prepend) {
        is LoadState.Loading -> {
            loadComposable.run { PrependLoading() }
        }

        is LoadState.NotLoading -> {
            loadComposable.run {
                PrependNotLoading(state.endOfPaginationReached)
            }
        }

        is LoadState.Error -> {
            loadComposable.run {
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
    loadComposable: LoadComposable
) {
    when (val state = lazyPagingItems.loadState.append) {
        is LoadState.Loading -> {
            loadComposable.run {
                AppendLoading()
            }
        }

        is LoadState.NotLoading -> {
            loadComposable.run {
                AppendNotLoading(state.endOfPaginationReached)
            }
        }

        is LoadState.Error -> {
            loadComposable.run {
                AppendError(state.error) {
                    lazyPagingItems.retry()
                }
            }
        }
    }
}
