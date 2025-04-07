package io.datou.develop

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

@PublishedApi
internal val MySharedPreferences: SharedPreferences by lazy {
    AppContext.getSharedPreferences(
        "${AppContext.packageName}_SPHelper_version_1",
        Context.MODE_PRIVATE
    )
}

fun <T> saveToShared(
    pair: Pair<String, T>,
    commit: Boolean = false
) = MySharedPreferences.edit(commit) {
    when (pair.second) {
        is Int -> putInt(pair.first, pair.second as Int)
        is Long -> putLong(pair.first, pair.second as Long)
        is Float -> putFloat(pair.first, pair.second as Float)
        is String -> putString(pair.first, pair.second as String)
        is Boolean -> putBoolean(pair.first, pair.second as Boolean)
        else -> throw IllegalArgumentException()
    }
}

inline fun <reified T> loadFromShared(pair: Pair<String, T>) = MySharedPreferences.run {
    when (pair.second) {
        is Int -> getInt(pair.first, pair.second as Int) as T
        is Long -> getLong(pair.first, pair.second as Long) as T
        is Float -> getFloat(pair.first, pair.second as Float) as T
        is String -> getString(pair.first, pair.second as String) as T
        is Boolean -> getBoolean(pair.first, pair.second as Boolean) as T
        else -> throw IllegalArgumentException()
    }
}
