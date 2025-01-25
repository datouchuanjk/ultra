package io.standard.tools

import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File
import java.io.InputStream

fun File.toProviderUri(
    authority: String = App.packageName + ".fileProvider"
): Uri? {
    return FileProvider.getUriForFile(App, authority, this)
}

fun <R> Uri.useStream(
    block: (InputStream) -> R
): R? {
    return try {
        App.contentResolver.openInputStream(this)?.use(block)
    } catch (e: Exception) {
        null
    }
}

