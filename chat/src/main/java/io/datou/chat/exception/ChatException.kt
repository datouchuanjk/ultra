package io.datou.chat.exception

class ChatException(val code: Int, message: String?) : Exception(message) {
    constructor(cause: Throwable?) : this(-1, cause?.message)
}