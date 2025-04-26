package io.datou.develop

import android.content.ContentUris
import android.content.ContentValues
import android.net.Uri
import android.provider.MediaStore

val ExternalFilesUri: Uri? = MediaStore.Files.getContentUri("external")

val ExternalImagesUri: Uri? = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

val ExternalAudioUri: Uri? = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI

val ExternalVideoUri: Uri? = MediaStore.Video.Media.EXTERNAL_CONTENT_URI

fun Uri.inputStream() = AppContext.contentResolver.openInputStream(this)

fun Uri.outputStream() = AppContext.contentResolver.openOutputStream(this)

fun Uri.insertOrUpdate(
    values: ContentValues,
    query: List<String>,
) = query(
    projection = arrayOf(MediaStore.MediaColumns._ID),
    selection = query.joinToString(" AND ") { "$it = ?" },
    selectionArgs = query.map { values.get(it).toString() }.toTypedArray()
)?.use {
    it.takeIf { it.moveToFirst() }?.getLongOrNull(MediaStore.MediaColumns._ID)?.let {
        ContentUris.withAppendedId(this, it)
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

