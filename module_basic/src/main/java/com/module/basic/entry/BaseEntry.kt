package com.module.basic.entry

data class BaseEntry<T>(
    val code: Int = 0,
    val message: String = "",
    val data: T? = null
) {
    val isSuccessful get() = code == 200
}
