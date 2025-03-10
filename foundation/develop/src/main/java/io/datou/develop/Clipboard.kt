package io.datou.develop

import android.content.ClipData
import android.content.ClipboardManager
import android.net.ConnectivityManager
import androidx.core.content.getSystemService

fun String.copyToClipboard(label: String? = null) {
    App.getSystemService<ClipboardManager>()?.setPrimaryClip(
        ClipData.newPlainText(label, this)
    )
}

