package io.datou.develop

import android.net.Uri
import android.os.Environment
import android.webkit.MimeTypeMap
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileInputStream
import java.security.MessageDigest

fun String.toFileInCacheDir() = toFileInDirectory(AppContext.cacheDir)

fun String.toFileInFilesDir(): File = toFileInDirectory(AppContext.filesDir)

fun String.toFileInExternalCacheDir() = toFileInDirectory(AppContext.externalCacheDir)

fun String.toFileInExternalFilesDir(
    type: String
) = toFileInDirectory(AppContext.getExternalFilesDir(type))

fun String.toFileInExternalPublicFilesDir(
    type: String
) = toFileInDirectory(Environment.getExternalStoragePublicDirectory(type))

fun String.toFileInDirectory(parent: File?) = File(parent, this)

val File.mimeType get() = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)

fun File.ensureCreated(
    isDirectory: Boolean = extension.isEmpty()
): Boolean {
    if (exists()) {
        return true
    }
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
        walk().forEach { file ->
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

fun File.toSharedUri(
    authority: String = "${AppContext.packageName}.fileProvider"
): Uri? = FileProvider.getUriForFile(AppContext, authority, this)



