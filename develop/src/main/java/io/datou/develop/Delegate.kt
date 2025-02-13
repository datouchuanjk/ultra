package io.datou.develop

import java.lang.reflect.Proxy

internal inline fun <reified T : Any> noOpDelegate(): T {
    val javaClass = T::class.java
    return Proxy.newProxyInstance(
        javaClass.classLoader, arrayOf(javaClass)
    ) { _, _, _ -> } as T
}
