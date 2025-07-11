package io.watermelon.util

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.IntRange
import java.io.ByteArrayOutputStream

fun Bitmap.toByteArray(
    @IntRange(from = 0) max: Int? = null,
): ByteArray = ByteArrayOutputStream().run {
    var quality = 100
    compress(Bitmap.CompressFormat.JPEG, quality, this)
    max?.let {
        require(it > 0)
        while (size() > it && quality > 10) {
            reset()
            quality -= 10
            compress(Bitmap.CompressFormat.JPEG, quality, this)
        }
    }
    toByteArray()
}

fun Bitmap.saveToGallery(
    context: Context,
    fileName: String = "${System.currentTimeMillis()}.PNG",
    mimeType: String = fileName.mimeType!!,
    directory: String = Environment.DIRECTORY_PICTURES,
    contentUri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
    onComplete: (Throwable?) -> Unit
) {
    toByteArray().inputStream().saveToMediaStore(
        context = context,
        fileName = fileName,
        mimeType = mimeType,
        directory = directory,
        contentUri = contentUri,
        onComplete = onComplete
    )
}




