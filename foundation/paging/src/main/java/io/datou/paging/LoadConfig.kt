package io.datou.paging

internal val DefaultLoadConfig = LoadConfig(
    loadSize = 20
)

data class LoadConfig(
    val loadSize: Int
)