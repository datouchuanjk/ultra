package io.watermelon.paging

data class LoadParams<Key>(
    val key: Key?,
    val pageSize: Int
)
