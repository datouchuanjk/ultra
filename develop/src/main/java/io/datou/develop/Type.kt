package io.datou.develop

import java.lang.reflect.ParameterizedType

@Suppress("UNCHECKED_CAST")
fun <T> Any.actualTypeArgument(index: Int): Class<T> {
    val type = javaClass.genericSuperclass as ParameterizedType
    return type.actualTypeArguments[index] as Class<T>
}