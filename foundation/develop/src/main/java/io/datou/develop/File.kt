package io.datou.develop

import android.os.Environment
import java.io.File
import java.io.FileInputStream
import java.security.MessageDigest

fun createFileInCacheDir(fileName: String): File {
    return File(InstanceApp.cacheDir, fileName).createAbsolutely()
}

fun createFileInFilesDir(fileName: String): File {
    return File(InstanceApp.filesDir, fileName).createAbsolutely()
}

fun createFileInExternalCacheDir(fileName: String): File {
    return File(InstanceApp.externalCacheDir, fileName).createAbsolutely()
}

fun createFileInExternalFilesDir(type: String?, fileName: String): File {
    return File(InstanceApp.getExternalFilesDir(type), fileName).createAbsolutely()
}

fun createFileInExternalStoragePublicDirectory(type: String, fileName: String): File {
    return File(Environment.getExternalStoragePublicDirectory(type), fileName).createAbsolutely()
}

fun createFileInExternalStorageDirectory(fileName: String): File {
    return File(Environment.getExternalStorageDirectory(), fileName).createAbsolutely()
}

fun File.createAbsolutely(): File {
    if (exists()) {
        return this
    }
    val parentFile = parentFile
    if (parentFile != null && !parentFile.exists()) {
        parentFile.mkdirs()
    }
    createNewFile()
    return this
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
