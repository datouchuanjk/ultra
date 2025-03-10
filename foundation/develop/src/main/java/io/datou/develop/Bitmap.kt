package io.datou.develop

import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.net.toUri
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

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

inline fun decodeToBitmap(
    reqWidth: Int,
    reqHeight: Int,
    block: (BitmapFactory.Options) -> Bitmap?
): Bitmap? {
    return BitmapFactory.Options().run {
        inJustDecodeBounds = true
        block(this)
        inSampleSize = run {
            val height = outHeight
            val width = outWidth
            var size = 1
            if (height > reqHeight || width > reqWidth) {
                val halfHeight = height / 2
                val halfWidth = width / 2
                while (halfHeight / size >= reqHeight && halfWidth / size >= reqWidth) {
                    size *= 2
                }
            }
            size
        }
        inJustDecodeBounds = false
        block(this)
    }
}

fun Bitmap.saveToGallery(
    fileName: String,
    format: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG,
    quality: Int = 100
): Uri? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        saveToGalleryAboveQ(fileName, format, quality)
    } else {
        saveToGalleryBelowQ(fileName, format, quality)
    }
}

private fun Bitmap.saveToGalleryAboveQ(
    fileName: String,
    format: Bitmap.CompressFormat,
    quality: Int
): Uri? {
    val values = ContentValues().apply {
        put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
        put(MediaStore.Images.Media.MIME_TYPE, "image/${format.name.lowercase()}")
        put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
        put(MediaStore.Images.Media.IS_PENDING, 1)
    }
    val contentResolver = App.contentResolver
    val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
    return try {
        uri?.apply {
            outputStream()?.use {
                compress(format, quality, it)
                it.flush()
            }
            values.put(MediaStore.Images.Media.IS_PENDING, 0)
            contentResolver.update(this, values, null, null)
        }
    } catch (e: Exception) {
        e.printStackTrace()
        uri?.let { contentResolver.delete(it, null, null) }
        throw e
    }
}

private fun Bitmap.saveToGalleryBelowQ(
    fileName: String,
    format: Bitmap.CompressFormat,
    quality: Int
): Uri {
    val file = createFileInExternalStoragePublicDirectory(
        Environment.DIRECTORY_PICTURES,
        "$fileName.${format.name.lowercase()}"
    )
    try {
        file.outputStream().use {
            compress(format, quality, it)
            it.flush()
        }
    } catch (e: Exception) {
        file.deleteRecursively()
        throw e
    }
    MediaScannerConnection.scanFile(
        App,
        arrayOf(file.absolutePath),
        arrayOf("image/${format.name.lowercase()}"),
        null
    )
    return file.toUri()
}
