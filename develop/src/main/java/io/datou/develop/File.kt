package io.datou.develop

import android.os.Environment
import android.util.Log
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.security.DigestInputStream
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

fun createFileInCacheDir(fileName: String): File {
    return File(App.cacheDir, fileName).createAbsolutely()
}

fun createFileInFilesDir(fileName: String): File {
    return File(App.filesDir, fileName).createAbsolutely()
}

fun createFileInExternalCacheDir(fileName: String): File {
    return File(App.externalCacheDir, fileName).createAbsolutely()
}

fun createFileInExternalFilesDir(type: String?, fileName: String): File {
    return File(App.getExternalFilesDir(type), fileName).createAbsolutely()
}

fun createFileInExternalStoragePublicDirectory(type: String, fileName: String): File {
    return File(Environment.getExternalStoragePublicDirectory(type), fileName).createAbsolutely()
}

internal fun File.createAbsolutely(): File {
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

fun File.totalSize(): Long {
    if (isFile) {
        return length()
    }
    var size = 0L
    walkTopDown().forEach { file ->
        size += if (file.isFile) file.length() else 0
    }
    return size
}

fun File.deleteRecursively(): Boolean {
    if (!exists()) {
        return true
    }
    if (isDirectory) {
        val children = listFiles()
        if (children != null) {
            for (child in children) {
                if (!child.deleteRecursively()) {
                    return false
                }
            }
        }
    }
    return delete()
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
