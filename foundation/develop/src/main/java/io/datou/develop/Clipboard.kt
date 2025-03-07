package io.datou.develop

import android.content.ClipData
import android.content.ClipboardManager
import android.net.ConnectivityManager
import androidx.core.content.getSystemService

fun String.clipboardCopy(label: String? = null) {
    val clipData = ClipData.newPlainText(label, this)
    App.getSystemService<ClipboardManager>()?.setPrimaryClip(clipData)
}