package io.composex.paging

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

fun <T> ViewModel.buildOffsetPaging(
    initialKey: Int = 1,
    config: PagingConfig = PagingConfig(),
    context: CoroutineContext = Dispatchers.IO,
    load: suspend (LoadParams<Int>) -> List<T>
) = buildPaging(
    initialKey = initialKey,
    config = config,
    context = context
) {
    val list = load(it)
    val nextKey = if (list.size < it.pageSize) null else it.key!! + 1
    LoadResult(nextKey, list)
}

fun <Key, Value> ViewModel.buildPaging(
    initialKey: Key? = null,
    config: PagingConfig = PagingConfig(),
    context: CoroutineContext = Dispatchers.IO,
    load: suspend (LoadParams<Key>) -> LoadResult<Key, Value>
): Paging<Value> {
    return PagingImpl(
        initialKey = initialKey,
        config = config,
        load = load,
        scope = viewModelScope,
        context = context
    )
}