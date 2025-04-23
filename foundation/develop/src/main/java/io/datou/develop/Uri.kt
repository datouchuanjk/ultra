package io.datou.develop

import android.content.ContentUris
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import kotlin.reflect.KTypeProjection

fun Uri.inputStream() = AppContext.contentResolver.openInputStream(this)

fun Uri.outputStream() = AppContext.contentResolver.openOutputStream(this)

fun Uri.delete(
    where: String? = null,
    selectionArgs: Array<String>? = null
) = AppContext.contentResolver.delete(this, where, selectionArgs)

fun Uri.update(
    values: ContentValues,
    where: String? = null,
    selectionArgs: Array<String>? = null,
) = AppContext.contentResolver.update(this, values, where, selectionArgs)

fun Uri.updatePendCompletion(
    where: String? = null,
    selectionArgs: Array<String>? = null,
) = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
    AppContext.contentResolver.update(
        this,
        ContentValues().apply {
            put(MediaStore.MediaColumns.IS_PENDING, 0)
        }, where, selectionArgs
    )
} else {
    0
}

fun Uri.query(
    projection: List<String>? = null,
    selection: List<String>? = null,
    selectionArgs: List<String>? = null,
    sortOrder: List<String>? = null,
    sortOrderType: String = "DESC"
) = AppContext.contentResolver.query(
    this,
    projection?.toTypedArray(),
    selection?.joinToString(" AND ") { "$it = ?" },
    selectionArgs?.toTypedArray(),
    sortOrder?.joinToString(" , ") { "$it  $sortOrderType" }
)?.use { cursor ->
    buildList {
        while (cursor.moveToNext()) {
            projection?.associate { columnName ->
                columnName to cursor.getColumnIndex(columnName)
                    .let { columnIndex ->
                        when (cursor.getType(columnIndex)) {
                            Cursor.FIELD_TYPE_NULL -> null
                            Cursor.FIELD_TYPE_INTEGER -> cursor.getLong(columnIndex)
                            Cursor.FIELD_TYPE_FLOAT -> cursor.getDouble(columnIndex)
                            Cursor.FIELD_TYPE_STRING -> cursor.getString(columnIndex)
                            Cursor.FIELD_TYPE_BLOB -> cursor.getBlob(columnIndex)
                            else -> null
                        }
                    }
            }?.let { pair ->
                add(pair)
            }
        }
    }
} ?: listOf()



