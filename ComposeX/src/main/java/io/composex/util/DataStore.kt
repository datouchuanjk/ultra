package io.composex.util

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

fun <T> DataStore<Preferences>.putAsFlow(
    key: Preferences.Key<T>,
    value: T
) = flow {
    edit { prefs ->
        prefs[key] = value
        emit(Unit)
    }
}

fun DataStore<Preferences>.putAsFlow(
    transform: suspend (MutablePreferences) -> Unit
) = flow {
    edit { prefs ->
        transform(prefs)
        emit(Unit)
    }
}

fun <T> DataStore<Preferences>.getAsFlow(
    key: Preferences.Key<T>,
) = data.map { prefs ->
    prefs[key]
}

fun <T> DataStore<Preferences>.removeAsFlow(
    key: Preferences.Key<T>,
) = flow {
    edit { prefs ->
        prefs.remove(key)
        emit(Unit)
    }
}

fun DataStore<Preferences>.getStringAsFlow(
    key: Preferences.Key<String>,
    defaultValue: String = "",
) = getAsFlow(key).map { it ?: defaultValue }

fun DataStore<Preferences>.getBoolAsFlow(
    key: Preferences.Key<Boolean>,
    defaultValue: Boolean = false,
) = getAsFlow(key).map { it ?: defaultValue }

fun DataStore<Preferences>.getIntAsFlow(
    key: Preferences.Key<Int>,
    defaultValue: Int = 0,
) = getAsFlow(key).map { it ?: defaultValue }

fun DataStore<Preferences>.getDoubleAsFlow(
    key: Preferences.Key<Double>,
    defaultValue: Double = 0.0,
) = getAsFlow(key).map { it ?: defaultValue }

fun DataStore<Preferences>.getLongAsFlow(
    key: Preferences.Key<Long>,
    defaultValue: Long = 0L,
) = getAsFlow(key).map { it ?: defaultValue }

fun DataStore<Preferences>.getFloatAsFlow(
    key: Preferences.Key<Float>,
    defaultValue: Float = 0F,
) = getAsFlow(key).map { it ?: defaultValue }

fun DataStore<Preferences>.getStringSetAsFlow(
    key: Preferences.Key<Set<String>>,
    defaultValue: Set<String> = setOf(),
) = getAsFlow(key).map { it ?: defaultValue }