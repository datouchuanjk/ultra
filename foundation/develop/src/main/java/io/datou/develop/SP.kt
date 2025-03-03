
package io.datou.develop

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

internal val InternalSP: SharedPreferences by lazy {
    App.getSharedPreferences(
        "${App.packageName}_SP_VERSION_1",
        Context.MODE_PRIVATE
    )
}

fun <T> spSave(key: String, value: T, commit: Boolean = false) {
    InternalSP.edit(commit) {
        when (value) {
            is Int -> putInt(key, value)
            is Long -> putLong(key, value)
            is Float -> putFloat(key, value)
            is String -> putString(key, value)
            is Boolean -> putBoolean(key, value)
            is Set<*> -> {
                require(value.all { it is String })
                @Suppress("UNCHECKED_CAST")
                putStringSet(key, value as Set<String>)
            }
        }
    }
}

fun spGetInt(key: String, defValue: Int = 0) = InternalSP.getInt(key, defValue)
fun spGetLong(key: String, defValue: Long = 0L) = InternalSP.getLong(key, defValue)
fun spGetFloat(key: String, defValue: Float = 0F) = InternalSP.getFloat(key, defValue)
fun spGetString(key: String, defValue: String = "") =
    InternalSP.getString(key, defValue) ?: defValue

fun spGetBoolean(key: String, defValue: Boolean = false) = InternalSP.getBoolean(key, defValue)
fun spGetStringSet(key: String, defValue: Set<String> = setOf()) =
    InternalSP.getStringSet(key, defValue) ?: defValue
