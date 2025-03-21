package io.datou.develop

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Parcelable
import androidx.core.net.toUri
import java.io.File
import java.io.Serializable

inline fun <reified T : Serializable> Intent.getSerializableExtraCompat(name: String): T? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getSerializableExtra(name, T::class.java)
    } else {
        getSerializableExtra(name) as? T
    }
}

inline fun <reified T : Parcelable> Intent.getParcelableExtraCompat(name: String): T? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getParcelableExtra(name, T::class.java)
    } else {
        getParcelableExtra(name)
    }
}


val Intent.isNullActivity get() = resolveActivity(InstanceApp.packageManager) == null

inline fun <reified T : Activity> intentOf(
    block: Intent.() -> Unit = {}
) = Intent(InstanceApp, T::class.java).apply(block)

fun installApk(
    apkFile: File,
    authority: String = "${InstanceApp.packageName}.fileProvider"
) = InstanceApp.startActivity(
    Intent(Intent.ACTION_VIEW).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        setDataAndType(
            apkFile.toSharedUri(authority),
            "application/vnd.android.package-archive"
        )
    }
)

fun jumpToBrowser(url: String) = InstanceApp.startActivity(
    Intent(Intent.ACTION_VIEW).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        setData(url.toUri())
    }
)




