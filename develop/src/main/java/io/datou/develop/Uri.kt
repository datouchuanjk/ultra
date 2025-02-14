package io.datou.develop

import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File
import java.io.InputStream

fun File.toProviderUri(
    authority: String = "${App.packageName}.fileProvider"
): Uri = FileProvider.getUriForFile(App, authority, this)

fun <R> Uri.use(
    block: (InputStream) -> R
) = App.contentResolver.openInputStream(this)?.use(block)

