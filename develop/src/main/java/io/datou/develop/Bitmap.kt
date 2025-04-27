package io.datou.develop

import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaScannerConnection
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Base64
import android.webkit.MimeTypeMap
import java.io.ByteArrayOutputStream
import java.io.File
import kotlin.io.extension

fun String.base64AsBitmap(): Bitmap? {
    if (!startsWith("data:")) {
        return null
    }
    val parts = split(",", limit = 2)
    if (parts.size < 2) {
        return null
    }
    return try {
        Base64.decode(parts[1], Base64.DEFAULT)
            .run {
                BitmapFactory.decodeByteArray(this, 0, size)
            }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun Bitmap.compressToByteArray(maxSize: Int): ByteArray {
    val outputStream = ByteArrayOutputStream()
    var quality = 100
    compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
    while (outputStream.toByteArray().size > maxSize && quality > 0) {
        outputStream.reset()
        quality -= 10
        compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
    }
    return outputStream.toByteArray()
}

fun Bitmap.saveToGallery(
    parent: String = Environment.DIRECTORY_PICTURES,
    name: String = "${AppContext.packageName}${File.separator}${System.currentTimeMillis()}.PNG",
    type: String = name.mimeType.orEmpty(),
    format: Bitmap.CompressFormat = Bitmap.CompressFormat.PNG,
    quality: Int = 100,
    onComplete: (() -> Unit)? = null
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        ExternalImagesUri?.insertOrUpdate(
            values = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, name)
                put(MediaStore.MediaColumns.MIME_TYPE, type)
                put(MediaStore.MediaColumns.RELATIVE_PATH, parent)
                put(MediaStore.MediaColumns.IS_PENDING, 1)
            },
            selection = "${MediaStore.MediaColumns.DISPLAY_NAME} = ? AND ${MediaStore.MediaColumns.MIME_TYPE} = ?",
            selectionArgs = arrayOf(name, type)
        )?.let { uri ->
            uri.outputStream()
                ?.use {
                    compress(format, quality, it)
                }
            uri.update(
                ContentValues().apply {
                    put(MediaStore.MediaColumns.IS_PENDING, 0)
                }
            )
            onComplete?.invoke()
        }
    } else {
        val file = File(
            Environment.getExternalStoragePublicDirectory(parent),
            name
        )
        file.parentFile?.mkdirs()
        file.outputStream().use {
            compress(format, quality, it)
        }
        MediaScannerConnection.scanFile(
            AppContext,
            arrayOf(file.absolutePath),
            null
        ) { _, _ ->
            onComplete?.invoke()
        }
    }
}




