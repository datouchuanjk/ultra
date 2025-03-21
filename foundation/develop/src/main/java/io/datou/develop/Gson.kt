package io.datou.develop

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

fun Any.toJson(): String = Gson().toJson(this).orEmpty()

inline fun <reified T> String.fromJson(): T? {
    return try {
        if (T::class.java.typeParameters.isNotEmpty()) {
            Gson().fromJson(this, object : TypeToken<T>() {}.type)
        } else {
            Gson().fromJson(this, T::class.java)
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
