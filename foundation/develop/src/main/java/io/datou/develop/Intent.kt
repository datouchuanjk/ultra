package io.datou.develop

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Parcelable
import androidx.core.net.toUri
import java.io.File
import java.io.Serializable

fun Intent.takeIfNotNullActivity() = takeIf { resolveActivity(Instance.packageManager) != null }

inline fun <reified T : Activity> intentOf(
    block: Intent.() -> Unit = {}
) = Intent(Instance, T::class.java).apply(block)

fun Context.installApk(
    apkFile: File,
    authority: String = "${Instance.packageName}.fileProvider"
) = Intent(Intent.ACTION_VIEW).apply {
    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    setDataAndType(
        apkFile.toProviderUri(authority),
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

fun Context.shareTo(
    text: String,
    packageName: String
) = Intent(Intent.ACTION_SEND).apply {
    type = "text/plain"
    putExtra(Intent.EXTRA_TEXT, text)
    setPackage(packageName)
}.takeIfNotNullActivity()
    ?.run {
        startActivity(this)
    }



