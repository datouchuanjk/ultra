package io.standard.composable.paging

internal val DefaultLoadConfig = LoadConfig(
    loadSize = 20
)

data class LoadConfig(
    val loadSize: Int
)