package io.datou.develop

import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.OutputStream
import kotlin.coroutines.resume
import kotlin.io.use

suspend fun saveToMediaStore(
    displayName: String,
    mimeType: String,
    relativePath: String,
    insertUri: Uri,
    isOverwrite: Boolean = false,
    block: (OutputStream) -> Unit
): Uri? {
    val contentResolver = Instance.contentResolver
    val values = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, displayName)
        put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            put(MediaStore.MediaColumns.RELATIVE_PATH, relativePath)
            put(MediaStore.MediaColumns.IS_PENDING, 1)
            if (isOverwrite) {
                val projection = arrayOf(MediaStore.MediaColumns._ID)
                val selection = buildString {
                    append(MediaStore.MediaColumns.DISPLAY_NAME)
                    append(" = ? ")
                    append(" AND ")
                    append(MediaStore.MediaColumns.RELATIVE_PATH)
                    append(" = ? ")
                    append(" AND ")
                    append(MediaStore.MediaColumns.MIME_TYPE)
                    append(" = ? ")
                }
                val selectionArgs = arrayOf(displayName, relativePath, mimeType)
                contentResolver.query(insertUri, projection, selection, selectionArgs, null)?.use {
                    if (it.moveToFirst()) {
                        val id = it.getLong(it.getColumnIndexOrThrow(MediaStore.MediaColumns._ID))
                        val existingUri = ContentUris.withAppendedId(insertUri, id)
                        contentResolver.delete(existingUri, null, null)
                    }
                }
            }
        } else {
            var file = fileInExternalStoragePublicDirectory(relativePath, displayName)
            if (file.exists()) {
                when (isOverwrite) {
                    true -> if (file.delete()) {
                        suspendCancellableCoroutine {
                            MediaScannerConnection.scanFile(
                                Instance,
                                arrayOf(file.absolutePath),
                                null
                            ) { _, _ ->
                                it.resume(Unit)
                            }
                        }
                    }

                    false -> {
                        val prev = displayName.substringBeforeLast(".")
                        val next = displayName.substringAfterLast(".", "")
                        var index = 1
                        while (file.exists()) {
                            file = fileInExternalStoragePublicDirectory(
                                relativePath,
                                buildString {
                                    append(prev)
                                    append(" (${index})")
                                    if (next.isNotEmpty()) {
                                        append(".")
                                        append(next)
                                    }
                                }
                            )
                            index++
                        }
                        put(MediaStore.MediaColumns.DISPLAY_NAME, file.absolutePath)
                    }
                }
            }
            file.createAbsolutely()
            put(MediaStore.MediaColumns.DATA, file.absolutePath)
        }
    }

    val uri = contentResolver.insert(insertUri, values)
    return try {
        uri?.apply {
            outputStream()?.use(block)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                values.put(MediaStore.MediaColumns.IS_PENDING, 0)
            }
            contentResolver.update(this, values, null, null)
        }
    } catch (e: Exception) {
        e.printStackTrace()
        uri?.let { contentResolver.delete(it, null, null) }
        throw e
    }
}

fun queryMediaFiles(
    displayName: String,
    mimeType: String,
    insertUri: Uri,
    isLike: Boolean = false
): List<Uri> {
    val contentResolver = Instance.contentResolver
    val projection = arrayOf(MediaStore.Files.FileColumns._ID)
    val selection = buildString {
        append(MediaStore.MediaColumns.DISPLAY_NAME)
        append(if (isLike) " LIKE  ? " else " = ? ")
        append(" AND ")
        append(MediaStore.MediaColumns.MIME_TYPE)
        append(" = ? ")
    }
    val selectionArgs = arrayOf(displayName.run {
        if (isLike) {
            val baseName = displayName.substringBeforeLast('.')
            val extension = displayName.substringAfterLast('.', "")
            if (extension.isNotEmpty()) {
                "%$baseName%.$extension"
            } else {
                "%$baseName%"
            }
        } else {
            this
        }
    }, mimeType)
    val uris = mutableListOf<Uri>()
    val cursor = contentResolver.query(insertUri, projection, selection, selectionArgs, null)
    cursor?.use {
        while (it.moveToNext()) {
            val id = it.getLong(it.getColumnIndexOrThrow(MediaStore.Files.FileColumns._ID))
            val fileUri = ContentUris.withAppendedId(insertUri, id)
            uris.add(fileUri)
        }
    }
    return uris
}


fun getFilePathFromContentUriPreQ(context: Context, uri: Uri): String? {
    var filePath: String? = null
    val projection = arrayOf(MediaStore.MediaColumns.DATA)
    val cursor = context.contentResolver.query(uri, projection, null, null, null)
    cursor?.use {
        if (it.moveToFirst()) {
            val columnIndex = it.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
            filePath = it.getString(columnIndex)
        }
    }
    return filePath
}