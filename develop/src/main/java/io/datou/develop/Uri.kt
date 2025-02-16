package io.datou.develop

import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File
import java.io.InputStream

fun File.toProvideUri(
    authority: String = "${App.packageName}.fileProvider"
): Uri? = FileProvider.getUriForFile(App, authority, this)

fun Uri.inputStream() = App.contentResolver.openInputStream(this)

fun Uri.outputStream() = App.contentResolver.openOutputStream(this)

