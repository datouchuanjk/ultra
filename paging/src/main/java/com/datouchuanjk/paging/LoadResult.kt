package com.datouchuanjk.paging

data class LoadResult<Key, Value>(
    val nextKey: Key?,
    val data: List<Value>
)
