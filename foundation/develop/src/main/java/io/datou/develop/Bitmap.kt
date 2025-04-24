package io.datou.develop

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.util.Base64
import java.io.ByteArrayOutputStream

fun String.base64decodeAsBitmap(): Bitmap? {
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

fun Bitmap.compressAsBytes(maxSize: Int): ByteArray {
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
    fileName: String,
    format: Bitmap.CompressFormat = Bitmap.CompressFormat.PNG,
    quality: Int = 100
) = fileName.asFileInExternalPublicFilesDir(
    Environment.DIRECTORY_PICTURES
).writeAs {
    compress(format, quality, it)
}




