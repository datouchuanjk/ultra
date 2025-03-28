package io.datou.develop

import android.os.Environment
import java.io.File
import java.io.FileInputStream
import java.security.MessageDigest

fun newFileInCacheDir(fileName: String): File {
    return File(Instance.cacheDir, fileName)
}

fun newFileInFilesDir(fileName: String): File {
    return File(Instance.filesDir, fileName)
}

fun newFileInExternalCacheDir(fileName: String): File {
    return File(Instance.externalCacheDir, fileName)
}

fun newFileInExternalFilesDir(type: String, fileName: String): File {
    return File(Instance.getExternalFilesDir(type), fileName)
}

fun newFileInExternalStoragePublicDirectory(type: String, fileName: String): File {
    return File(Environment.getExternalStoragePublicDirectory(type), fileName)
}

fun File.renameUntilNonExistent(
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
