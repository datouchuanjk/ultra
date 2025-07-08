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
    Intent(Intent.ACTION_VIEW).apply {
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        setDataAndType(
            apkFile.toProviderUri(this@installApk, authority),
            "application/vnd.android.package-archive"
        )
    }.takeUnless {
        it.isNullActivity(this)
    }?.also {
        startActivity(it)
    }
}

fun Context.jumpBrowser(url: String) {
    Intent(Intent.ACTION_VIEW, url.toUri())
        .takeUnless {
            it.isNullActivity(this)
        }?.also {
            startActivity(it)
        }
}




