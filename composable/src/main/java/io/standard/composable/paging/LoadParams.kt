package io.standard.composable.paging

data class LoadParams<T>(
    val key: T?,
    val config: LoadConfig,
)