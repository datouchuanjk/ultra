package io.watermelon.util

import android.content.Context
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import java.io.File
import java.io.FileNotFoundException
import java.io.InputStream

fun InputStream.saveToMediaStore(
    context: Context,
    fileName: String,
    mimeType: String,
    directory: String,
    contentUri: Uri?,
    onComplete: (Throwable?) -> Unit
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        saveToMediaStoreAboveQ( context, fileName, mimeType, directory, contentUri, onComplete)
    } else {
        saveToMediaStoreBelowQ( context, fileName, mimeType, directory, onComplete)
    }
}

private fun InputStream.saveToMediaStoreAboveQ(
    context: Context,
    fileName: String,
    mimeType: String,
    directory: String,
    contentUri: Uri?,
    onComplete: (Throwable?) -> Unit
) {
    var uri: Uri? = null
    try {
        uri = contentUri?.insert(context) {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
            put(MediaStore.MediaColumns.RELATIVE_PATH, directory)
            put(MediaStore.MediaColumns.IS_PENDING, 1)
        } ?: throw NullPointerException()
        uri.openOutputStream(context)?.use { output ->
            use { input->
                input.copyTo(output)
            }
        }
        uri.update(context) {
            put(MediaStore.MediaColumns.IS_PENDING, 0)
        }
        onComplete.invoke(null)
    } catch (e: Exception) {
        e.printStackTrace()
        uri?.let { context.contentResolver.delete(it, null, null) }
        onComplete.invoke(e)
    }
}

private fun InputStream.saveToMediaStoreBelowQ(
    context: Context,
    fileName: String,
    mimeType: String,
    directory: String,
    onComplete: (Throwable?) -> Unit
) {
    var target: File? = null
    try {
        target = File(
            Environment.getExternalStoragePublicDirectory(directory),
            fileName
        ).ensureUnique()
        if (!target.ensureExists()) throw FileNotFoundException()
        target.outputStream().use { output ->
            use { input->
                input.copyTo(output)
            }
        }
        onComplete.invoke(null)
        MediaScannerConnection.scanFile(
            context,
            arrayOf(target.absolutePath),
            arrayOf(mimeType)
        ) { _, _ -> }
    } catch (e: Exception) {
        e.printStackTrace()
        target?.delete()
        onComplete.invoke(e)
    }
}
