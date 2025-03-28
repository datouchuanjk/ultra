package io.datou.develop

import android.content.ContentValues
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import java.io.File
import java.io.OutputStream
import kotlin.io.use

private fun ContentValues.setDisplayNameAndInferMimeType(value: String) = apply {
    put(MediaStore.MediaColumns.DISPLAY_NAME, value)
    File(value).extension.run {
        MimeTypeMap.getSingleton().getMimeTypeFromExtension(this)
    }?.let {
        put(MediaStore.MediaColumns.MIME_TYPE, it)
    }
}

private fun ContentValues.setFilePathCompat(value: String) = apply {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        put(MediaStore.MediaColumns.RELATIVE_PATH, value)
    } else {
        val displayName = get(MediaStore.MediaColumns.DISPLAY_NAME) as? String
        checkNotNull(displayName)
        val file = newFileInExternalStoragePublicDirectory(
            value,
            displayName
        ).renameUntilNonExistent()
        file.createAbsolutely()
        put(MediaStore.MediaColumns.DATA, value)
    }
}


private fun ContentValues.insetToContentUri(
    contentUri: Uri = MediaStore.Files.getContentUri("external"),
    block: (Uri) -> Unit
): Uri? {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        put(MediaStore.MediaColumns.IS_PENDING, 1)
    }
    return Instance.contentResolver.insert(contentUri, this)?.let {
        block(it)
        Instance.contentResolver.update(it, this, null, null)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            put(MediaStore.MediaColumns.IS_PENDING, 0)
        }
        it
    }
}

fun saveToMediaStore(
    displayName: String,
    mimeType: String,
    relativePath: String,
    insertUri: Uri,
    block: (OutputStream) -> Unit
): Uri? {
    ContentValues()
        .setDisplayNameAndInferMimeType(displayName)
        .setFilePathCompat(relativePath)
        .insetToContentUri(insertUri) {
            try {
                it.outputStream()?.use(block)
            } catch (e: Exception) {
                it.delete()
                e.printStackTrace()
                throw e
            }
        }
}


