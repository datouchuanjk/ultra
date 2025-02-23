package io.datou.chat.data

data class ChatResponse<T>(
    val data: List<T>,
    val cursor: String?,
    val isNoMoreData: Boolean
)