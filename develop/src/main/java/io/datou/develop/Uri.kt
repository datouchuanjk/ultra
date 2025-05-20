package io.datou.develop

import android.content.ContentUris
import android.content.ContentValues
import android.net.Uri
import android.provider.MediaStore
import androidx.core.database.getLongOrNull

internal val ExternalFilesUri: Uri? = MediaStore.Files.getContentUri("external")

internal val ExternalImagesUri: Uri? = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

internal val ExternalAudioUri: Uri? = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI

internal val ExternalVideoUri: Uri? = MediaStore.Video.Media.EXTERNAL_CONTENT_URI

fun Uri.inputStream() = AppContext.contentResolver.openInputStream(this)

fun Uri.outputStream() = AppContext.contentResolver.openOutputStream(this)

fun Uri.insertOrUpdate(
    values: ContentValues,
    selection: String? = null,
    selectionArgs: Array<String>? = null
) = query(
    projection = arrayOf(MediaStore.MediaColumns._ID),
    selection = selection,
    selectionArgs = selectionArgs
)?.use { it ->
    it.takeIf { it.moveToFirst() }
        ?.getLongOrNull(it.getColumnIndex(MediaStore.MediaColumns._ID))
        ?.let {
            ContentUris.withAppendedId(this, it)
        }?.also {
            it.update(values)
        }
} ?: insert(values)

fun Uri.insert(
    values: ContentValues
) = AppContext.contentResolver.insert(
    this,
    values
)

fun Uri.delete(
    where: String? = null,
    selectionArgs: Array<String>? = null
) = AppContext.contentResolver.delete(
    this,
    where,
    selectionArgs
)

fun Uri.update(
    values: ContentValues,
    where: String? = null,
    selectionArgs: Array<String>? = null
) = AppContext.contentResolver.update(
    this,
    values,
    where,
    selectionArgs
)

fun Uri.query(
    projection: Array<String>? = null,
    selection: String? = null,
    selectionArgs: Array<String>? = null,
    sortOrder: String? = null
) = AppContext.contentResolver.query(
    this,
    projection,
    selection,
    selectionArgs,
    sortOrder
)

