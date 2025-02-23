package io.datou.develop

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Parcelable
import java.io.File
import java.io.FileNotFoundException
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

fun Intent.exists() = resolveActivity(App.packageManager) != null

inline fun <reified T : Activity> intentOf(
    block: Intent.() -> Unit = {}
) = Intent(App, T::class.java).apply(block)

fun installApk(
    apkFile: File,
    authority: String = App.packageName + ".fileProvider"
) = App.startActivity(
    Intent(Intent.ACTION_VIEW).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        setDataAndType(
            apkFile.toSharedUri(authority = authority),
            "application/vnd.android.package-archive"
        )
    }
)

fun wakeApp(block: ((Intent) -> Unit)? = null) {
    StackTopActivity?.let {
        App.startActivity(Intent(App, it::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            block?.invoke(this)
        })
    }
}

