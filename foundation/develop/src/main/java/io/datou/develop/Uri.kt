package io.datou.develop

import android.content.ContentUris
import android.net.Uri
import android.provider.MediaStore
import androidx.core.content.FileProvider
import java.io.File

fun File.toSharedUri(
    authority: String = "${Instance.packageName}.fileProvider"
): Uri? = FileProvider.getUriForFile(Instance, authority, this)

fun Uri.inputStream() = Instance.contentResolver.openInputStream(this)

fun Uri.outputStream() = Instance.contentResolver.openOutputStream(this)

val Uri.ID
    get() = ContentUris.parseId(this)

fun Long.generateMediaUriFromContentUri(
    contentUri: Uri = MediaStore.Files.getContentUri("external")
): Uri? {
    return ContentUris.withAppendedId(contentUri, this)
}

fun Uri.delete(
    where: String? = null,
    selectionArgs: Array<String>? = null
) = Instance.contentResolver.delete(this, where, selectionArgs)
