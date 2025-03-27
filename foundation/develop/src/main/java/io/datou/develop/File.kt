package io.datou.develop

import android.os.Environment
import android.webkit.MimeTypeMap
import java.io.File
import java.io.FileInputStream
import java.security.MessageDigest

fun fileInCacheDir(fileName: String): File {
    return File(Instance.cacheDir, fileName)
}

fun fileInFilesDir(fileName: String): File {
    return File(Instance.filesDir, fileName)
}

fun fileInExternalCacheDir(fileName: String): File {
    return File(Instance.externalCacheDir, fileName)
}

fun fileInExternalFilesDir(type: String?, fileName: String): File {
    return File(Instance.getExternalFilesDir(type), fileName)
}

fun fileInExternalStoragePublicDirectory(type: String, fileName: String): File {
    return File(Environment.getExternalStoragePublicDirectory(type), fileName)
}

fun File.createAbsolutely(): Boolean {
    if (exists()) {
        return true
    }
    val isDirectory = absolutePath.lastIndexOf(".") == -1
    if (isDirectory) {
        return mkdirs()
    } else {
        val parentFile = parentFile
        if (parentFile != null && !parentFile.exists()) {
            if (!parentFile.mkdirs()) {
                return false
            }
        }
        return createNewFile()
    }
}

val File.totalSize: Long
    get() {
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
