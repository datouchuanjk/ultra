package com.module.basic.vo

data class BaseVO<T>(
    val code: Int = 0,
    val message: String = "",
    val data: T? = null
) {
    val isSuccessful get() = code == 200
}
