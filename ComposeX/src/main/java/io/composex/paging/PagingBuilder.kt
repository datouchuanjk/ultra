package io.composex.paging

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

fun <T> ViewModel.buildOffsetPaging(
    initialKey: Int = 1,
    config: PagingConfig = PagingConfig(),
    load: suspend (LoadParams<Int>) -> List<T>
) = buildPaging(
    initialKey = initialKey,
    config = config
) {
    val list = load(it)
    val nextKey = if (list.size < it.pageSize) null else it.key!! + 1
    LoadResult(nextKey, list)
}

fun <Key, Value> ViewModel.buildPaging(
    initialKey: Key? = null,
    config: PagingConfig = PagingConfig(),
    load: suspend (LoadParams<Key>) -> LoadResult<Key, Value>
): Paging<Value> {
    return PagingImpl(
        initialKey = initialKey,
        config = config,
        load = load,
        scope = viewModelScope
    )
}