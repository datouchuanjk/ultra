package io.datou.develop

import android.content.ClipData
import android.content.ClipboardManager
import androidx.core.content.getSystemService

fun String.copyToClipboard(label: String? = null) = AppContext
    .getSystemService<ClipboardManager>()
    ?.setPrimaryClip(ClipData.newPlainText(label, this))

