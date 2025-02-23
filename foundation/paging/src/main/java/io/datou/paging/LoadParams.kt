package io.datou.paging

data class LoadParams<T>(
    val key: T?,
    val config: LoadConfig,
)