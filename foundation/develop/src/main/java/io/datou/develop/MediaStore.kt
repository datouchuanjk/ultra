package io.datou.develop

import android.content.ContentUris
import android.content.ContentValues
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import java.io.File
import java.io.InputStream
import java.io.OutputStream
import kotlin.io.extension
import kotlin.text.startsWith

fun File.asMediaStoreCompat(): MediaStoreCompat? {
    return MediaStoreCompatImpl(this).takeIf { it.check() }
}

interface MediaStoreCompat {
    fun delete()
    fun useOutputStream(block: (OutputStream) -> Unit)
    fun useInputStream(block: (InputStream) -> Unit)
}

internal class MediaStoreCompatImpl(
    private val file: File,
) : MediaStoreCompat {

    private companion object {
        private const val DISPLAY_NAME_SELECTION = "${MediaStore.MediaColumns.DISPLAY_NAME} = ?"
        private const val MIME_TYPE_SELECTION = "${MediaStore.MediaColumns.MIME_TYPE} = ?"
        private val FILES_EXTERNAL_CONTENT_URI = MediaStore.Files.getContentUri("external")
        private val IMAGES_EXTERNAL_CONTENT_URI = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        private val VIDEO_EXTERNAL_CONTENT_URI = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        private val AUDIO_EXTERNAL_CONTENT_URI = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
    }

    private val _isAndroid10 by lazy {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
    }

    private val _mediaStorePath by lazy {
        Environment.getExternalStoragePublicDirectory("").absolutePath
    }

    private val _displayName by lazy {
        file.name
    }

    private val _type by lazy {
        file.parent?.substring(_mediaStorePath.length)?.trimStart(File.separatorChar)!!
    }

    private val _mimeType by lazy {
        MimeTypeMap.getSingleton().getMimeTypeFromExtension(file.extension)
    }

    private val _contentUri: Uri by lazy {
        _mimeType?.run {
            when {
                startsWith("image/") -> IMAGES_EXTERNAL_CONTENT_URI
                startsWith("video/") -> VIDEO_EXTERNAL_CONTENT_URI
                startsWith("audio/") -> AUDIO_EXTERNAL_CONTENT_URI
                else -> FILES_EXTERNAL_CONTENT_URI
            }
        } ?: FILES_EXTERNAL_CONTENT_URI
    }

    private val _contentValues by lazy {
        ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, _displayName)
            put(MediaStore.MediaColumns.MIME_TYPE, _mimeType)
            put(MediaStore.MediaColumns.RELATIVE_PATH, _type)
            put(MediaStore.MediaColumns.IS_PENDING, 1)
        }
    }

    private val _insertUri: Uri? by lazy {
        Instance.contentResolver.insert(
            _contentUri,
            _contentValues
        )
    }

    private val _existsUri: Uri? by lazy {
        Instance.contentResolver.query(
            _contentUri,
            arrayOf(MediaStore.MediaColumns._ID),
            buildString {
                append(DISPLAY_NAME_SELECTION)
                append(" AND ")
                append(MIME_TYPE_SELECTION)
            },
            arrayOf(_displayName, _mimeType),
            null
        )?.use {
            val idColumnIndex = it.getColumnIndexOrThrow(MediaStore.MediaColumns._ID)
            if (it.moveToFirst()) {
                val id = it.getLong(idColumnIndex)
                ContentUris.withAppendedId(_contentUri, id)
            } else {
                null
            }
        }
    }

    internal  fun check(): Boolean {
        if (!file.absolutePath.startsWith(_mediaStorePath)) {
            return false
        }
        if (file.extension.isEmpty()) {
            return false
        }
        return true
    }

    override fun delete() {
        if (_isAndroid10) {
            _existsUri?.delete()
        } else {
            file.delete()
            MediaScannerConnection.scanFile(
                Instance,
                arrayOf(file.absolutePath),
                null
            ) { _, _ -> }
        }
    }

    override fun useOutputStream(block: (OutputStream) -> Unit) {
        if (_isAndroid10) {
            if (_existsUri == null) {
                try {
                    _insertUri?.outputStream()?.use(block)
                    _insertUri?.updatePendCompletion()
                } catch (e: Exception) {
                    e.printStackTrace()
                    _insertUri?.delete()
                    throw e
                }
            } else {
                _existsUri?.outputStream()?.use(block)
                _existsUri?.update()
            }
        } else {
            file.outputStream().use(block)
        }
    }

    override fun useInputStream(block: (InputStream) -> Unit) {
        if (_isAndroid10) {
            _existsUri?.inputStream()?.use(block)
        } else {
            file.inputStream().use(block)
        }
    }
}






