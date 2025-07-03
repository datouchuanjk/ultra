package io.composex.util

import android.content.ContentValues
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
    contentUri: Uri,
    onComplete: (Throwable?) -> Unit
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val values = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
            put(MediaStore.MediaColumns.RELATIVE_PATH, directory)
            put(MediaStore.MediaColumns.IS_PENDING, 1)
        }
        var uri: Uri? = null
        try {
            uri = context.contentResolver.insert(contentUri, values)
            if (uri != null) {
                context.contentResolver.openOutputStream(uri)?.use { output ->
                    this.copyTo(output)
                }
                context.contentResolver.update(uri, ContentValues().apply {
                    put(MediaStore.MediaColumns.IS_PENDING, 0)
                }, null, null)
                onComplete.invoke(null)
            } else {
                throw NullPointerException()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            onComplete.invoke(e)
            uri?.let { context.contentResolver.delete(it, null, null) }
        }
    } else {
        try {
            val target = File(
                Environment.getExternalStoragePublicDirectory(directory),
                fileName
            ).ensureUniqueName()
            if (target.ensureExists()) {
                target.outputStream().use { output ->
                    this.copyTo(output)
                }
                onComplete.invoke(null)
                MediaScannerConnection.scanFile(
                    context,
                    arrayOf(target.absolutePath),
                    arrayOf(mimeType)
                ) { _, _ -> }
            } else {
                throw FileNotFoundException()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            onComplete.invoke(e)
        }
    }
}