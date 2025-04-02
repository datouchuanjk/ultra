package io.datou.develop

import android.content.ContentUris
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onCompletion

fun Uri.inputStream() = Instance.contentResolver.openInputStream(this)

fun Uri.outputStream() = Instance.contentResolver.openOutputStream(this)

fun Uri.delete(
    where: String? = null,
    selectionArgs: Array<String>? = null
) = Instance.contentResolver.delete(this, where, selectionArgs)

inline fun Uri.update(
    where: String? = null,
    selectionArgs: Array<String>? = null,
    block: ContentValues.() -> Unit = {},
) = Instance.contentResolver.update(this, ContentValues().apply(block), where, selectionArgs)

fun Uri.updatePendCompletion(
    where: String? = null,
    selectionArgs: Array<String>? = null,
) = Instance.contentResolver.update(
    this,
    ContentValues().apply {
        put(MediaStore.MediaColumns.IS_PENDING, 0)
    }, where, selectionArgs
)

val Uri.id get() = ContentUris.parseId(this)

val Uri.displayName
    get() = queryMediaColumns(MediaStore.MediaColumns.DISPLAY_NAME).firstOrNull() as? String

val Uri.mimeType
    get() = queryMediaColumns(MediaStore.MediaColumns.MIME_TYPE).firstOrNull() as? String

val Uri.relativePath
    get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        queryMediaColumns(MediaStore.MediaColumns.RELATIVE_PATH).firstOrNull() as? String
    } else null

val Uri.data
    get() = queryMediaColumns(MediaStore.MediaColumns.DATA).firstOrNull() as? String

fun Uri.queryMediaColumns(vararg columns: String): List<Any?> {
    return Instance.contentResolver.query(this, columns, null, null, null)?.use { cursor ->
        val resultMap = mutableListOf<Any?>()
        if (cursor.moveToFirst()) {
            columns.forEach { column ->
                val columnIndex = cursor.getColumnIndex(column)
                if (columnIndex != -1) {
                    when (cursor.getType(columnIndex)) {
                        Cursor.FIELD_TYPE_NULL -> resultMap.add(null)
                        Cursor.FIELD_TYPE_INTEGER -> resultMap.add(cursor.getLong(columnIndex))
                        Cursor.FIELD_TYPE_FLOAT -> resultMap.add(cursor.getDouble(columnIndex))
                        Cursor.FIELD_TYPE_STRING -> resultMap.add(cursor.getString(columnIndex))
                        Cursor.FIELD_TYPE_BLOB -> resultMap.add(cursor.getBlob(columnIndex))
                        else -> resultMap.add(null)
                    }
                } else {
                    resultMap.add(null)
                }
            }
        }
        resultMap
    } ?: listOf()
}


