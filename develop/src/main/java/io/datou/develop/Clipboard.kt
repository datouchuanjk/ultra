package io.datou.develop

import android.content.ClipData
import android.content.ClipboardManager
import android.net.ConnectivityManager
import androidx.core.content.getSystemService

internal val InternalClipboardManager by lazy {
    App.getSystemService<ClipboardManager>()
}

fun clipboardCopy(text: String) {
    val clipData = ClipData.newPlainText(null, text)
    InternalClipboardManager?.setPrimaryClip(clipData)
}