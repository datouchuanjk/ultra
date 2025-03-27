package io.datou.develop

import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Base64
import androidx.core.net.toUri
import java.io.ByteArrayOutputStream

fun String.base64ToBitmap(): Bitmap {
    return Base64.decode(split(",")[1], Base64.DEFAULT).run {
        BitmapFactory.decodeByteArray(this, 0, this.size)
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

inline fun decodeBitmapWithSizeConstraint(
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
    fileName: String = "${System.currentTimeMillis()}.jpeg",
    format: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG,
    quality: Int = 100
): Uri? {
    return null
}
