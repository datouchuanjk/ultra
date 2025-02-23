package io.datou.develop

import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File

fun File.toSharedUri(
    authority: String = "${App.packageName}.fileProvider"
): Uri? = FileProvider.getUriForFile(App, authority, this)

fun Uri.inputStream() = App.contentResolver.openInputStream(this)

fun Uri.outputStream() = App.contentResolver.openOutputStream(this)

