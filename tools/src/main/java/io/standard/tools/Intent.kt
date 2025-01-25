package io.standard.tools

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Parcelable
import java.io.Serializable

inline fun <reified T : Serializable> Intent.getSerializableExtraCompat(name: String): T? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getSerializableExtra(name, T::class.java)
    } else {
        @Suppress("DEPRECATION")
        getSerializableExtra(name) as? T
    }
}

inline fun <reified T : Parcelable> Intent.getParcelableExtraCompat(name: String): T? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getParcelableExtra(name, T::class.java)
    } else {
        @Suppress("DEPRECATION")
        getParcelableExtra(name)
    }
}

val AppIntent get() = App.packageManager.getLaunchIntentForPackage(App.packageName)

val Intent.isActivityExists get() = resolveActivity(App.packageManager) != null

inline fun <reified T : Activity> intentOf(block: Intent.() -> Unit = {}) =
    Intent(App, T::class.java).apply(block)

inline fun intentOf(block: Intent.() -> Unit) = Intent().apply(block)

