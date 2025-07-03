package io.composex.util

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import java.io.File

fun Intent.isNullActivity(context: Context) = resolveActivity(context.packageManager) == null

fun Context.installApk(
    apkFile: File,
    authority: String = "${packageName}.fileProvider"
) {
    val intent = Intent(Intent.ACTION_VIEW).apply {
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        setDataAndType(
            apkFile.toProviderUri(this@installApk, authority),
            "application/vnd.android.package-archive"
        )
    }
    if (!intent.isNullActivity(this)) {
        startActivity(intent)
    }
}

fun Context.jumpBrowser(url: String) {
    val intent = Intent(Intent.ACTION_VIEW, url.toUri())
    if (!intent.isNullActivity(this)) {
        startActivity(intent)
    }
}




