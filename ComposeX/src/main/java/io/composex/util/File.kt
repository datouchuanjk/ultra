package io.composex.util

import android.content.Context

import android.net.Uri
import android.webkit.MimeTypeMap
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileInputStream
import java.security.MessageDigest

val File.mimeType get() = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)

fun File.ensureUniqueName(): File {
    if (!exists()) return this
    val extension = if (extension.isBlank()) "" else ".$extension"
    var counter = 1
    var newFile: File
    do {
        newFile = File(parentFile, "$nameWithoutExtension ($counter)$extension")
        counter++
    } while (newFile.exists())
    return newFile
}

fun File.ensureExists(
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

fun File.toProviderUri(
    context: Context,
    authority: String = "${context.packageName}.fileProvider"
): Uri? = FileProvider.getUriForFile(context, authority, this)



