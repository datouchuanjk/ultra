package io.datou.develop

import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File

fun File.toSharedUri(
    authority: String = "${InstanceApp.packageName}.fileProvider"
): Uri? = FileProvider.getUriForFile(InstanceApp, authority, this)

fun Uri.inputStream() = InstanceApp.contentResolver.openInputStream(this)

fun Uri.outputStream() = InstanceApp.contentResolver.openOutputStream(this)

