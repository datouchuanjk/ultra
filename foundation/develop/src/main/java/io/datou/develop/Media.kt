package io.datou.develop

import android.content.ContentResolver
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.webkit.MimeTypeMap
import java.io.OutputStream

fun saveToMediaStore(
    displayName: String,
    mimeType: String? = MimeTypeMap.getFileExtensionFromUrl(displayName).run {
        MimeTypeMap.getSingleton().getMimeTypeFromExtension(lowercase())
    },
    relativePath: String? = getRelativePathByMimeType(mimeType),
    insertUri: Uri? = getInsertUriByMimeType(mimeType),
    block: (OutputStream) -> Unit
): Uri? {
    checkNotNull(mimeType)
    checkNotNull(relativePath)
    checkNotNull(insertUri)
    val contentResolver = InstanceApp.contentResolver
    val values = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, displayName)
        put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
        put(MediaStore.MediaColumns.RELATIVE_PATH, relativePath)
        put(MediaStore.MediaColumns.IS_PENDING, 1)
    }
    val uri = contentResolver.insert(insertUri, values)
    Log.e("1234","uri=$uri")
    return try {
        uri?.apply {
            this.outputStream()?.use(block)
            values.put(MediaStore.MediaColumns.IS_PENDING, 0)
            contentResolver.update(this, values, null, null)
        }
    } catch (e: Exception) {
        e.printStackTrace()
        uri?.let { contentResolver.delete(it, null, null) }
        throw e
    }
}

fun queryFromMediaStore(
    displayName: String,
    mimeType: String? = MimeTypeMap.getFileExtensionFromUrl(displayName).run {
        MimeTypeMap.getSingleton().getMimeTypeFromExtension(lowercase())
    },
    insertUri: Uri? = getInsertUriByMimeType(mimeType),
): Uri? {
    val uri = insertUri ?: MediaStore.Files.getContentUri("external")
    val contentResolver: ContentResolver = InstanceApp.contentResolver
    val projection = arrayOf(MediaStore.Files.FileColumns._ID)
    val selectionList = mutableListOf<String>()
    val selectionArgsList = mutableListOf<String>()
    selectionList.add("${MediaStore.Files.FileColumns.DISPLAY_NAME} = ?")
    selectionArgsList.add(displayName)
    if (mimeType != null) {
        selectionList.add("${MediaStore.Files.FileColumns.MIME_TYPE} = ?")
        selectionArgsList.add(mimeType)
    }
    val selection = selectionList.joinToString(" AND ")
    val selectionArgs = selectionArgsList.toTypedArray()
    val cursor: Cursor? = contentResolver.query(
        uri,
        projection,
        selection,
        selectionArgs,
        null
    )
    val result = mutableListOf<Uri>()
    cursor?.use {
        while (it.moveToNext()) {
            val idColumnIndex = it.getColumnIndex(MediaStore.Files.FileColumns._ID)
            val id = it.getLong(idColumnIndex)
            val fileUri = Uri.withAppendedPath(uri, id.toString())
            result.add(fileUri)
        }
    }
    return result.firstOrNull()
}

private fun getInsertUriByMimeType(mimeType: String?): Uri? {
    return when {
        mimeType == null -> null
        mimeType.startsWith("image/") -> MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        mimeType.startsWith("audio/") -> MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        mimeType.startsWith("video/") -> MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        else -> MediaStore.Files.getContentUri("external")
    }
}

private fun getRelativePathByMimeType(mimeType: String?): String? {
    return when {
        mimeType == null -> null
        mimeType.startsWith("image/") -> Environment.DIRECTORY_PICTURES
        mimeType.startsWith("audio/") -> Environment.DIRECTORY_MUSIC
        mimeType.startsWith("video/") -> Environment.DIRECTORY_MOVIES
        mimeType.startsWith("application/vnd.android.package-archive") -> Environment.DIRECTORY_DOWNLOADS
        mimeType.startsWith("application/") -> Environment.DIRECTORY_DOCUMENTS
        else -> Environment.DIRECTORY_DOWNLOADS
    }
}

