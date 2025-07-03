package io.composex.paging

data class LoadParams<Key>(
    val key: Key?,
    val pageSize: Int
)
