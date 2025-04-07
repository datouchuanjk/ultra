package io.datou.develop

import android.content.ContentUris
import android.content.ContentValues
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.io.OutputStream
import java.security.MessageDigest
import kotlin.text.startsWith

fun String.asFileInCacheDir(): File {
    return File(AppContext.cacheDir, this)
}

fun String.asFileInFilesDir(): File {
    return File(AppContext.filesDir, this)
}

fun String.asFileInExternalCacheDir(): File {
    return File(AppContext.externalCacheDir, this)
}

fun String.asFileInExternalFilesDir(type: String): File {
    return File(AppContext.getExternalFilesDir(type), this)
}

fun String.asFileInExternalPublicFilesDir(type: String): File {
    return File(Environment.getExternalStoragePublicDirectory(type), this)
}

val File.mimeType get() = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)

val File.externalPublicRelativePath
    get() = absolutePath.replace(Environment.getExternalStoragePublicDirectory("").absolutePath, "")
        .trimStart(File.separatorChar)
        .substringBeforeLast(File.separatorChar)

val File.baseName get() = name.substringBeforeLast('.')

fun File.findFileUriInMediaStore(): Uri? = contentUri?.run {
    AppContext.contentResolver.query(
        this,
        arrayOf(MediaStore.MediaColumns._ID),
        buildString {
            append("${MediaStore.MediaColumns.DISPLAY_NAME} = ?")
            if (!mimeType.isNullOrEmpty()) {
                append(" AND ")
                append("${MediaStore.MediaColumns.MIME_TYPE} = ?")
            }
        },
        if (!mimeType.isNullOrEmpty()) {
            arrayOf(name, mimeType)
        } else {
            arrayOf(name)
        },
        null
    )?.use {
        val idColumnIndex = it.getColumnIndexOrThrow(MediaStore.MediaColumns._ID)
        if (it.moveToFirst()) {
            val id = it.getLong(idColumnIndex)
            ContentUris.withAppendedId(this, id)
        } else {
            null
        }
    }
}

val File.contentUri: Uri?
    get() = mimeType.run {
        when {
            isNullOrEmpty() -> MediaStore.Files.getContentUri("external")
            startsWith("image/") -> MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            startsWith("video/") -> MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            startsWith("audio/") -> MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            else -> MediaStore.Files.getContentUri("external")
        }
    }

fun File.insertFileIntoMediaStore(): Uri? = contentUri?.run {
    AppContext.contentResolver.insert(
        this,
        ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
            put(MediaStore.MediaColumns.RELATIVE_PATH, externalPublicRelativePath)
            put(MediaStore.MediaColumns.IS_PENDING, 1)
        }
    )
}

fun File.createFileOrDirectory(): Boolean {
    if (exists()) {
        return true
    }
    val isDirectory = extension.isEmpty()
    if (isDirectory) {
        return mkdirs()
    } else {
        parentFile?.run {
            if (!exists() && !mkdirs()) {
                return false
            }
        }
        return createNewFile()
    }
}

val File.totalSize: Long
    get() {
        if (!exists()) {
            return 0
        }
        if (isFile) {
            return length()
        }
        var size = 0L
        walkTopDown().forEach { file ->
            size += if (file.isFile) file.length() else 0
        }
        return size
    }

val File.md5: String
    get() {
        val digest = MessageDigest.getInstance("MD5")
        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
        FileInputStream(this).use { input ->
            var bytesRead: Int
            while (input.read(buffer).also { bytesRead = it } != -1) {
                digest.update(buffer, 0, bytesRead)
            }
        }
        return digest.digest().joinToString("") { "%02x".format(it) }
    }

fun File.toProviderUri(
    authority: String = "${AppContext.packageName}.fileProvider"
): Uri? = FileProvider.getUriForFile(AppContext, authority, this)

fun <T> File.useOutputStreamCompat(block: (OutputStream) -> T): T? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val queryUri: Uri? = findFileUriInMediaStore()
        if (queryUri == null) {
            var insertUri: Uri? = null
            try {
                insertUri = insertFileIntoMediaStore()
                insertUri?.outputStream()
                    ?.let(block)
                    .apply {
                        insertUri?.updatePendCompletion()
                    }
            } catch (e: Exception) {
                e.printStackTrace()
                insertUri?.delete()
                null
            }
        } else {
            queryUri.outputStream()?.let(block)
        }
    } else {
        outputStream().let(block)
    }
}

fun <T> File.useInputStreamCompat(block: (InputStream) -> T) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val queryUri: Uri? = findFileUriInMediaStore()
        queryUri?.inputStream()?.let(block)
    } else {
        inputStream().let(block)
    }
}

