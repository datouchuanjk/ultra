package io.datou.develop

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

@PublishedApi
internal val AppSharedPreferences: SharedPreferences by lazy {
    AppContext.getSharedPreferences(
        "${AppContext.packageName}_SPHelper_version_1",
        Context.MODE_PRIVATE
    )
}

fun <T> T.persistByKey(key:String,commit: Boolean = false) {
    AppSharedPreferences.edit(commit) {
        when ( val value = this@persistByKey) {
            is Int -> putInt(key, value as Int)
            is Float -> putFloat(key, value as Float)
            is Long -> putLong(key, value as Long)
            is String -> putString(key, value as String)
            is Boolean -> putBoolean(key, value as Boolean)
            is Set<*> -> putStringSet(key, value as Set<String>)
            else -> {
                throw IllegalArgumentException("Unsupported type: ${value!!::class.java.simpleName}")
            }
        }
    }
}

fun getPersistByKey(key:String,defaultValue:Boolean =false){
    AppSharedPreferences.getBoolean(key,defaultValue)
}


