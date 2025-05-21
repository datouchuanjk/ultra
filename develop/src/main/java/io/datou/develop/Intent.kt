package io.datou.develop

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import java.io.File

fun Intent.takeIfNotNullActivity() = takeIf { resolveActivity(AppContext.packageManager) != null }


inline fun <reified T : Activity> intentOf(
) = Intent(AppContext, T::class.java)

inline fun <reified T : Activity> intentOf(
    block: (Intent.() -> Unit)
) = Intent(AppContext, T::class.java).apply(block)

fun Context.installApk(
    apkFile: File,
    authority: String = "${AppContext.packageName}.fileProvider"
) = Intent(Intent.ACTION_VIEW).apply {
    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    setDataAndType(
        apkFile.toSharedUri(authority),
        "application/vnd.android.package-archive"
    )
}.takeIfNotNullActivity()
    ?.run {
        startActivity(this)
    }

fun Context.jumpToBrowser(url: String) = Intent(Intent.ACTION_VIEW, url.toUri())
    .takeIfNotNullActivity()
    ?.run {
        startActivity(this)
    }

fun Context.shareTextTo(
    text: String,
    packageName: String? = null
) = Intent(Intent.ACTION_SEND).apply {
    type = "text/plain"
    putExtra(Intent.EXTRA_TEXT, text)
}.run {
    if (packageName == null) {
        Intent.createChooser(this, "share to")
    } else {
        this.setPackage(packageName)
    }
}.takeIfNotNullActivity()
    ?.run {
        startActivity(this)
    }



