package io.standard.composable.paging

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

fun <T, R> ViewModel.buildPagingController(
    context: CoroutineContext = Dispatchers.IO,
    initialKey: T? = null,
    config: LoadConfig = DefaultLoadConfig,
    load: suspend (LoadParams<T>) -> LoadResult<T, R>
): PagingController<R> {
    return buildMutablePagingController(
        scope = viewModelScope,
        context = context,
        initialKey = initialKey,
        config = config,
        load = load
    )
}

interface PagingController<T> {
    val count: Int
    val loadMoreState: LoadState
    val refreshState: LoadState
    operator fun get(index: Int): T
    fun refresh()
    fun loadMore()
    fun retry()
}
