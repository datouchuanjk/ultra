package io.datou.develop

import android.content.ContentUris
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.annotation.RequiresApi


@RequiresApi(Build.VERSION_CODES.Q)
fun test() {
    val values = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, "")
        put(MediaStore.MediaColumns.MIME_TYPE, "")
        put(MediaStore.MediaColumns.RELATIVE_PATH, "")
        put(MediaStore.MediaColumns.IS_PENDING, 1)
    }
    //实现插入功能
    ExternalFilesUri?.insertAsUri(values)?.apply {
        outputStream()?.use {
            it.write("".toByteArray())
        }
        insertComplete()
    }
}

val ExternalFilesUri: Uri? = MediaStore.Files.getContentUri("external")
val ExternalImagesUri: Uri? = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
val ExternalAudioUri: Uri? = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
val ExternalVideoUri: Uri? = MediaStore.Video.Media.EXTERNAL_CONTENT_URI

fun Uri.inputStream() = AppContext.contentResolver.openInputStream(this)

fun Uri.outputStream() = AppContext.contentResolver.openOutputStream(this)

fun Uri.delete(
    buildAction: (QueryBuilder.() -> Unit)? = null
) = QueryBuilder.withBuild(buildAction).let {
    AppContext.contentResolver.delete(this, it?.selection, it?.selectionArgs)
}

fun Uri.update(
    values: ContentValues,
    buildAction: (QueryBuilder.() -> Unit)? = null
) = QueryBuilder.withBuild(buildAction).let {
    AppContext.contentResolver.update(this, values, it?.selection, it?.selectionArgs)
}

@RequiresApi(Build.VERSION_CODES.Q)
fun Uri.insertAsUri(values: ContentValues): Uri? {
    return AppContext.contentResolver.insert(this, values)
}

@RequiresApi(Build.VERSION_CODES.Q)
fun Uri.insertComplete(): Int {
    val values = ContentValues().apply {
        put(MediaStore.MediaColumns.IS_PENDING, 0)
    }
    return update(values)
}

fun Uri.queryAsUri(
    buildAction: QueryBuilder.() -> Unit
) = query(buildAction)?.map {
    it[MediaStore.MediaColumns._ID]
}?.map {
    ContentUris.withAppendedId(this, it as Long)
}

fun Uri.query(
    buildAction: QueryBuilder.() -> Unit
) = QueryBuilder.withBuild(buildAction).let { builder ->
    AppContext.contentResolver.query(
        this,
        builder?.projection,
        builder?.selection,
        builder?.selectionArgs,
        builder?.sortOrder
    )?.use { cursor ->
        buildList {
            while (cursor.moveToNext()) {
                builder?.projection?.associate { columnName ->
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
    }
}


class QueryBuilder() {

    companion object {

        fun withBuild(buildAction: (QueryBuilder.() -> Unit)? = null) = buildAction?.let {
            QueryBuilder().apply(it)
        }
    }

    private var _projection: MutableList<String>? = null
    private var _selection: MutableList<String>? = null
    private var _selectionArgs: MutableList<String>? = null
    private var _sortOrder: MutableList<String>? = null

    internal fun withProjection(value: String) {
        if (_projection == null) {
            _projection = mutableListOf<String>()
        }
        _projection?.add(value)
    }

    fun withSelection(pair: Pair<String, String>, where: String = "=") {
        if (_selection == null) {
            _selection = mutableListOf()
        }
        if (_selectionArgs == null) {
            _selectionArgs = mutableListOf()
        }
        _selection?.add("${pair.first} $where ?")
        _selectionArgs?.add(pair.second)
    }

    fun withSortOrder(value: String, order: String = "DESC") {
        if (_sortOrder == null) {
            _sortOrder = mutableListOf()
        }
        _sortOrder?.add("$value  $order")
    }

    internal val projection get() = _projection?.toTypedArray()
    internal val selection get() = _selection?.joinToString(" AND ")
    internal val selectionArgs get() = _selectionArgs?.toTypedArray()
    internal val sortOrder get() = _sortOrder?.joinToString(" , ")
}

