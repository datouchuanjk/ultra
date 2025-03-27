package io.datou.develop

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
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


val Intent.isNullActivity get() = resolveActivity(Instance.packageManager) == null

inline fun <reified T : Activity> intentOf(
    block: Intent.() -> Unit = {}
) = Intent(Instance, T::class.java).apply(block)

fun installApk(
    apkFile: File,
    authority: String = "${Instance.packageName}.fileProvider"
) = Instance.startActivity(
    Intent(Intent.ACTION_VIEW).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        setDataAndType(
            apkFile.toSharedUri(authority),
            "application/vnd.android.package-archive"
        )
    }
)

fun jumpToBrowser(url: String) = Instance.startActivity(
    Intent(Intent.ACTION_VIEW).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        setData(url.toUri())
    }
)

fun String.shareTo(
    packageName: String? = null,
    openShareFailed: () -> Unit = {}
) {
    var intent = Intent(Intent.ACTION_SEND)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    intent.type = "text/plain"
    intent.putExtra(Intent.EXTRA_TEXT, this)
    val isAppInstalled = if (packageName.isNullOrEmpty()) {
        intent = Intent.createChooser(intent, "share to")
        true
    } else {
        try {
            Instance.packageManager.getPackageInfo(packageName, 0)
            intent.setPackage(packageName)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
    if (isAppInstalled && !intent.isNullActivity) {
        Instance.startActivity(intent)
    } else {
        openShareFailed()
    }
}




