package io.datou.develop

import android.os.Environment
import java.io.File

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
