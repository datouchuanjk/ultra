package io.watermelon.paging

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

fun <T> buildOffsetPaging(
    scope: CoroutineScope,
    initialKey: Int = 1,
    config: PagingConfig = PagingConfig(),
    context: CoroutineContext = Dispatchers.IO,
    load: suspend (LoadParams<Int>) -> List<T>
) = buildPaging(
    scope=scope,
    initialKey = initialKey,
    config = config,
    context = context
) {
    val list = load(it)
    val nextKey = if (list.size < it.pageSize) null else it.key!! + 1
    LoadResult(nextKey, list)
}

fun <Key, Value> buildPaging(
    scope: CoroutineScope,
    initialKey: Key? = null,
    config: PagingConfig = PagingConfig(),
    context: CoroutineContext = Dispatchers.IO,
    load: suspend (LoadParams<Key>) -> LoadResult<Key, Value>
): Paging<Value> {
    return PagingImpl(
        initialKey = initialKey,
        config = config,
        load = load,
        scope = scope,
        context = context
    )
}