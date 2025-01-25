package io.standard.tools

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

private val Preferences: SharedPreferences by lazy {
    App.getSharedPreferences("${App.packageName}_preferences_1", Context.MODE_PRIVATE)
}
val PreferencesPutBool: (String, () -> Boolean) -> Unit = { key, value ->
    Preferences.edit(true) {
        putBoolean(key, value())
    }
}

val PreferencesPutString: (String, () -> String) -> Unit = { key, value ->
    Preferences.edit(true) {
        putString(key, value())
    }
}

val PreferencesPutInt: (String, () -> Int) -> Unit = { key, value ->
    Preferences.edit(true) {
        putInt(key, value())
    }
}

val PreferencesPutLong: (String, () -> Long) -> Unit = { key, value ->
    Preferences.edit(true) {
        putLong(key, value())
    }
}

val PreferencesPutFloat: (String, () -> Float) -> Unit = { key, value ->
    Preferences.edit(true) {
        putFloat(key, value())
    }
}

val PreferencesGetBool: (String, () -> Boolean) -> Boolean = { key, value ->
    Preferences.getBoolean(key, value())
}

val PreferencesGetString: (String, () -> String) -> String = { key, value ->
    Preferences.getString(key, value()) ?: value()
}

val PreferencesGetInt: (String, () -> Int) -> Int = { key, value ->
    Preferences.getInt(key, value())
}

val PreferencesGetLong: (String, () -> Long) -> Long = { key, value ->
    Preferences.getLong(key, value())
}

val PreferencesGetFloat: (String, () -> Float) -> Float = { key, value ->
    Preferences.getFloat(key, value())
}


