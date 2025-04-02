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
import android.util.Log
import androidx.core.net.toUri
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

fun String.base64ToBitmap(): Bitmap {
    return Base64.decode(split(",")[1], Base64.DEFAULT)
        .run {
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

fun Bitmap.saveToGallery(
    fileName: String,
    format: Bitmap.CompressFormat = Bitmap.CompressFormat.PNG,
    quality: Int = 100
) = fileName
    .toFileInExternalPublicFilesDir(Environment.DIRECTORY_PICTURES)
    .asMediaStoreCompat()
    ?.useOutputStream {
        compress(format, quality, it)
    }



