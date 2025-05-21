package io.datou.develop

import android.os.Environment
import java.io.File

object FileUtils {

    // Create a File instance in the external public directory
    fun String.toExternalPublicDir(): File {
        val externalDir = AppContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
        return File(externalDir, this)
    }

    // Create a File instance in the internal cache directory
    fun String.toInternalCacheDir(): File {
        val cacheDir = AppContext.cacheDir
        return File(cacheDir, this)
    }

    // Create a File instance in the internal files directory
    fun String.toInternalFilesDir(): File {
        val filesDir = AppContext.filesDir
        return File(filesDir, this)
    }

    // Create a File instance in the external private directory
    fun String.toExternalPrivateDir(): File? {
        val externalFilesDir = AppContext.getExternalFilesDir(null)
        return externalFilesDir?.let { File(it, this) }
    }

    // Create a File instance in the external cache directory
    fun String.toExternalCacheDir(): File? {
        val externalCacheDir = AppContext.externalCacheDir
        return externalCacheDir?.let { File(it, this) }
    }
}
