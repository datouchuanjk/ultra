package com.datouchuanjk.paging

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

fun <Value> buildOffsetPaging(
    scope: CoroutineScope,
    context: CoroutineContext = Dispatchers.IO,
    initialKey: Int = 1,
    config: PagingConfig = PagingConfig(),
    load: suspend (LoadParams<Int>) -> List<Value>
) = buildPaging(
    scope = scope,
    context = context,
    initialKey = initialKey,
    config = config,
) {
    val list = load(it)
    val nextKey = if (list.size < it.pageSize) null else it.key!! + 1
    LoadResult(nextKey, list)
}

fun <Key, Value> buildPaging(
    scope: CoroutineScope,
    context: CoroutineContext = Dispatchers.IO,
    initialKey: Key? = null,
    config: PagingConfig = PagingConfig(),
    load: suspend (LoadParams<Key>) -> LoadResult<Key, Value>
): Paging<Value> {
    return PagingImpl(
        scope = scope,
        context = context,
        initialKey = initialKey,
        config = config,
        load = load,
    )
}