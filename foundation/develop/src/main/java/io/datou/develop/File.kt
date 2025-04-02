package io.datou.develop

import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.security.MessageDigest

fun String.toFileInCacheDir(): File {
    return File(Instance.cacheDir, this)
}

fun String.toFileInFilesDir(): File {
    return File(Instance.filesDir, this)
}

fun String.toFileInExternalCacheDir(): File {
    return File(Instance.externalCacheDir, this)
}

fun String.toFileInExternalFilesDir(type: String): File {
    return File(Instance.getExternalFilesDir(type), this)
}

fun String.toFileInExternalPublicFilesDir(type: String): File {
    return File(Environment.getExternalStoragePublicDirectory(type), this)
}

fun File.renameIfExists(
    block: File.(Int) -> String = { "$nameWithoutExtension ($it)" }
): File {
    val extension = if (extension.isNotEmpty()) ".${extension}" else ""
    var index = 1
    var file = this
    while (file.exists()) {
        val newName = block(index) + extension
        file = File(parentFile, newName)
        index++
    }
    return file
}

fun File.createAbsolutely(): Boolean {
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
    authority: String = "${Instance.packageName}.fileProvider"
): Uri? = FileProvider.getUriForFile(Instance, authority, this)

