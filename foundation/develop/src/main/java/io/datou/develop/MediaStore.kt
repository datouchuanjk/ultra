package io.datou.develop

import android.content.ContentUris
import android.content.ContentValues
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.webkit.MimeTypeMap
import java.io.File
import java.io.OutputStream
import kotlin.io.use

fun ContentValues.setDisplayNameAndInferMimeType(value: String) = apply {
    put(MediaStore.MediaColumns.DISPLAY_NAME, value)
    File(value).extension.run {
        MimeTypeMap.getSingleton().getMimeTypeFromExtension(this)
    }?.let {
        put(MediaStore.MediaColumns.MIME_TYPE, it)
    }
}

fun ContentValues.generateContentUriByMimeType(): Uri {
    return checkNotNull(getAsString(MediaStore.MediaColumns.MIME_TYPE)).run {
        when {
            startsWith("image/") -> MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            startsWith("video/") -> MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            startsWith("audio/") -> MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            else -> MediaStore.Files.getContentUri("external")
        }
    }
}

fun ContentValues.setFilePathCompat(value: String) = apply {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        put(MediaStore.MediaColumns.RELATIVE_PATH, value)
    } else {
        val displayName = checkNotNull(getAsString(MediaStore.MediaColumns.DISPLAY_NAME))
        val file = newFileInExternalPublicDir(
            value,
            displayName
        ).renameUntilNonExistent()
        file.createAbsolutely()
        put(MediaStore.MediaColumns.DATA, file.absolutePath)
        put(MediaStore.MediaColumns.DISPLAY_NAME, file.name)
    }
}

fun ContentValues.insertToMediaStore(
    contentUri: Uri = generateContentUriByMimeType(),
    block: (OutputStream) -> Unit
): Uri? {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        put(MediaStore.MediaColumns.IS_PENDING, 1)
    }
    return Instance.contentResolver.insert(contentUri, this)?.also { uri ->
        try {
            uri.outputStream()?.use(block)
        } catch (e: Exception) {
            e.printStackTrace()
            uri.delete()
            throw e
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            uri.update { put(MediaStore.MediaColumns.IS_PENDING, 0) }
        }
    }
}

fun <T> ContentValues.generateSelectionRule(block: ContentValues.(String, Array<String>) -> T): T {
    val selectionList = mutableListOf<String>()
    val selectionArgsList = mutableListOf<String>()
    for (key in keySet()) {
        val value = get(key)
        selectionList.add("$key = ?")
        selectionArgsList.add(value.toString())
    }
    val selection = selectionList.joinToString(" AND ")
    val selectionArgs = selectionArgsList.toTypedArray()
    return block(selection, selectionArgs)
}

fun ContentValues.queryFromMediaStore(
    contentUri: Uri = generateContentUriByMimeType(),
): List<Uri> {
    return generateSelectionRule { selection, selectionArgs ->
        Instance.contentResolver.query(
            contentUri,
            arrayOf(MediaStore.MediaColumns._ID),
            selection,
            selectionArgs,
            null
        )?.use {
            val uriList = mutableListOf<Uri>()
            val idColumnIndex = it.getColumnIndexOrThrow(MediaStore.MediaColumns._ID)
            while (it.moveToNext()) {
                val id = it.getLong(idColumnIndex)
                val uri = ContentUris.withAppendedId(contentUri, id)
                uriList.add(uri)
            }
            uriList
        } ?: listOf()
    }
}

fun ContentValues.deleteFromMediaStore(
    contentUri: Uri = generateContentUriByMimeType(),
    onScanFileFinish: (() -> Unit)? = null
): Int {
    return generateSelectionRule { selection, selectionArgs ->
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val count = contentUri.delete(selection, selectionArgs)
            onScanFileFinish?.invoke()
            count
        } else {
            queryFromMediaStore(contentUri)
                .mapNotNull {
                    it.data
                }.let { absolutePaths ->
                    if (absolutePaths.isEmpty()) {
                        onScanFileFinish?.invoke()
                        return@let 0
                    }
                    absolutePaths.forEach {
                        val file = File(it)
                        if (file.exists()) {
                            file.delete()
                        }
                    }
                    MediaScannerConnection.scanFile(
                        Instance,
                        absolutePaths.toTypedArray(),
                        null
                    ) { _, _ -> onScanFileFinish?.invoke() }
                    absolutePaths.size
                }
        }
    }
}


