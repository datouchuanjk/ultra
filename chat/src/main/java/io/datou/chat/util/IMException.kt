package io.datou.chat.util


class IMException(val code: Int, message: String?) : Exception(message) {
    constructor(cause: Throwable?) : this(-1, cause?.message)
}