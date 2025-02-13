package io.datou.develop

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

internal val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "${App.packageName}_preferences")
internal val appDataStore = App.dataStore

fun <T> savePreference(key: Preferences.Key<T>, value: T) {
    AppScope.launch {
        appDataStore.edit {
            it[key] = value
        }
    }
}

internal fun <T> observePreference(
    key: Preferences.Key<T>,
    defaultValue: T
) = appDataStore.data.map {
    it[key] ?: defaultValue
}

fun observeStringPreference(
    key: Preferences.Key<String>,
    defaultValue: String = ""
) = observePreference(key, defaultValue)

fun observeBoolPreference(
    key: Preferences.Key<Boolean>,
    defaultValue: Boolean = false
) = observePreference(key, defaultValue)

fun observeFloatPreference(
    key: Preferences.Key<Float>,
    defaultValue: Float = 0f
) = observePreference(key, defaultValue)

fun observeLongPreference(
    key: Preferences.Key<Long>,
    defaultValue: Long = 0L
) = observePreference(key, defaultValue)

fun observeIntPreference(
    key: Preferences.Key<Int>,
    defaultValue: Int = 0
) = observePreference(key, defaultValue)

fun observeDoublePreference(
    key: Preferences.Key<Double>,
    defaultValue: Double = 0.0
) = observePreference(key, defaultValue)

fun observeSetPreference(
    key: Preferences.Key<Set<String>>,
    defaultValue: Set<String> = setOf()
) = observePreference(key, defaultValue)

fun observeByteArrayPreference(
    key: Preferences.Key<ByteArray>,
    defaultValue: ByteArray = byteArrayOf()
) = observePreference(key, defaultValue)



