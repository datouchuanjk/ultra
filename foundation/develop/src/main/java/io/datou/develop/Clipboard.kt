package io.datou.develop

import android.content.ClipData
import android.content.ClipboardManager
import android.net.ConnectivityManager
import androidx.core.content.getSystemService

fun String.clipboardCopy() {
    val clipData = ClipData.newPlainText(null, this)
    App.getSystemService<ClipboardManager>()?.setPrimaryClip(clipData)
}