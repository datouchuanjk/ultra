package io.datou.develop

import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaScannerConnection
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Base64
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.OutputStream

fun String.toBitmap(): Bitmap? {
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

fun Bitmap.toByteArray(maxSize: Int? = null): ByteArray {
    val outputStream = ByteArrayOutputStream()
    var quality = 100
    compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
    maxSize?.let {
        require(it > 0)
        while (outputStream.toByteArray().size > it && quality > 0) {
            outputStream.reset()
            quality -= 10
            compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
        }
    }
    return outputStream.toByteArray()
}

fun Bitmap.saveToGallery(
    directory: String = Environment.DIRECTORY_PICTURES,
    file: String = "${AppContext.packageName}${File.separator}${System.currentTimeMillis()}.PNG",
    mimeType: String = file.mimeType.orEmpty(),
    use: (OutputStream) -> Unit = {
        compress(Bitmap.CompressFormat.PNG, 100, it)
    },
    onComplete: (() -> Unit)? = null
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        ExternalImagesUri?.insertOrUpdate(
            values = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, file)
                put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
                put(MediaStore.MediaColumns.RELATIVE_PATH, directory)
                put(MediaStore.MediaColumns.IS_PENDING, 1)
            },
            selection = "${MediaStore.MediaColumns.DISPLAY_NAME} = ? AND ${MediaStore.MediaColumns.MIME_TYPE} = ?",
            selectionArgs = arrayOf(file, mimeType)
        )?.let { uri ->
            uri.outputStream()?.use(use)
            uri.update(
                ContentValues().apply {
                    put(MediaStore.MediaColumns.IS_PENDING, 0)
                }
            )
            onComplete?.invoke()
        }
    } else {
        val target = File(
            Environment.getExternalStoragePublicDirectory(directory),
            file
        )
        target.parentFile?.mkdirs()
        target.outputStream().use(use)
        MediaScannerConnection.scanFile(
            AppContext,
            arrayOf(target.absolutePath),
            null
        ) { _, _ ->
            onComplete?.invoke()
        }
    }
}




