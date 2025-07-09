package io.composex.util

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.suspendCancellableCoroutine

suspend fun DataStore<Preferences>.handle(
    transform: suspend (MutablePreferences) -> Unit
) = try {
    edit { transform(it) }
    true
} catch (e: Exception) {
    e.printStackTrace()
    false
}

suspend fun <T> DataStore<Preferences>.put(
    key: Preferences.Key<T>,
    value: T
) = handle {
    it[key] = value
}

suspend fun <T> DataStore<Preferences>.remove(
    key: Preferences.Key<T>,
) = handle {
    it.remove(key)
}

suspend fun <T> DataStore<Preferences>.get(
    key: Preferences.Key<T>,
) = getAsFlow(key).firstOrNull()

fun <T> DataStore<Preferences>.getAsFlow(
    key: Preferences.Key<T>,
) = data.map { prefs ->
    prefs[key]
}


