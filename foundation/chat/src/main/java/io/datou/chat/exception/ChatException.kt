package io.datou.chat.exception

class ChatException(private val code: Int, message: String?) : Exception(message) {
    constructor(cause: Throwable?) : this(-1, cause?.message)

    override fun toString(): String {
        return "code:$code message:$message"
    }
}