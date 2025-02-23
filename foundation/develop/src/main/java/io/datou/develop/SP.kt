package io.datou.develop

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

object SP {
    private const val NAME = "SP_VERSION_1"

    private val _sp: SharedPreferences by lazy {
        App.getSharedPreferences(
            "${App.packageName}_${NAME}",
            Context.MODE_PRIVATE
        )
    }

    fun <T> put(key: String, value: T, commit: Boolean = false) {
        _sp.edit(commit) {
            when (value) {
                is Int -> putInt(key, value)
                is Long -> putLong(key, value)
                is Float -> putFloat(key, value)
                is String -> putString(key, value)
                is Boolean -> putBoolean(key, value)
                is Set<*> -> {
                    require(value.all { it is String })
                    putStringSet(key, value as Set<String>)
                }
            }
        }
    }

    fun getInt(key: String, defValue: Int = 0) = _sp.getInt(key, defValue)
    fun getLong(key: String, defValue: Long = 0L) = _sp.getLong(key, defValue)
    fun getFloat(key: String, defValue: Float = 0F) = _sp.getFloat(key, defValue)
    fun getString(key: String, defValue: String = "") = _sp.getString(key, defValue) ?: defValue
    fun getBoolean(key: String, defValue: Boolean = false) = _sp.getBoolean(key, defValue)
    fun getStringSet(key: String, defValue: Set<String> = setOf()) =
        _sp.getStringSet(key, defValue) ?: defValue
}