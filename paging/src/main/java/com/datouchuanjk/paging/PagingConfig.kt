package com.datouchuanjk.paging

data class PagingConfig(
    val pageSize: Int = 10,
    val prefetchDistance: Int = 1
)
