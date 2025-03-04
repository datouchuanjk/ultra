package io.datou.develop

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

object SPHelper {
    private const val SHARED_PREFERENCES_NAME = "SPHelper_version_1"

    private val _sharedPreferences: SharedPreferences by lazy {
        App.getSharedPreferences(
            "${App.packageName}_${SHARED_PREFERENCES_NAME}",
            Context.MODE_PRIVATE
        )
    }

    fun put(
        pair: Pair<String, Int>,
        commit: Boolean = false
    ) = _sharedPreferences.edit(commit) {
        putInt(pair.first, pair.second)
    }

    fun put(
        pair: Pair<String, String>,
        commit: Boolean = false
    ) = _sharedPreferences.edit(commit) {
        putString(pair.first, pair.second)
    }

    fun put(
        pair: Pair<String, Boolean>,
        commit: Boolean = false
    ) = _sharedPreferences.edit(commit) {
        putBoolean(pair.first, pair.second)
    }

    fun get(
        key: String,
        defValue: Int = 0
    ) = _sharedPreferences.getInt(key, defValue)

    fun get(
        key: String,
        defValue: String = ""
    ) = _sharedPreferences.getString(key, defValue) ?: defValue

    fun get(
        key: String,
        defValue: Boolean = false
    ) = _sharedPreferences.getBoolean(key, defValue)

}