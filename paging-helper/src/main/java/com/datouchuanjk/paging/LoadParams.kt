package com.datouchuanjk.paging

data class LoadParams<Key>(
    val key: Key?,
    val pageSize: Int
)
