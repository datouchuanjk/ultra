package io.datou.develop

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import androidx.core.os.bundleOf
import java.io.Serializable

inline fun <reified T : Serializable> Bundle.getSerializableOrNull(key: String): T? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getSerializable(key, T::class.java)
    } else {
        getSerializable(key) as? T
    }
}

inline fun <reified T : Parcelable> Bundle.getParcelableOrNull(key: String): T? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getParcelable(key, T::class.java)
    } else {
        getParcelable(key)
    }
}

inline fun <reified T : Serializable> Intent.getSerializableOrNull(name: String): T? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getSerializableExtra(name, T::class.java)
    } else {
        getSerializableExtra(name) as? T
    }
}

inline fun <reified T : Parcelable> Intent.getParcelableOrNull(name: String): T? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getParcelableExtra(name, T::class.java)
    } else {
        getParcelableExtra(name)
    }
}