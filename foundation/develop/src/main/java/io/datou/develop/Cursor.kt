package io.datou.develop

import android.content.ContentUris
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import androidx.core.database.getBlobOrNull
import androidx.core.database.getDoubleOrNull
import androidx.core.database.getFloatOrNull
import androidx.core.database.getIntOrNull
import androidx.core.database.getLongOrNull
import androidx.core.database.getShortOrNull
import androidx.core.database.getStringOrNull

fun Cursor.getLongOrNull(columnName: String) = getLongOrNull(getColumnIndex(columnName))

fun Cursor.getStringOrNull(columnName: String) = getStringOrNull(getColumnIndex(columnName))

fun Cursor.getFloatOrNull(columnName: String) = getFloatOrNull(getColumnIndex(columnName))

fun Cursor.getShortOrNull(columnName: String) = getShortOrNull(getColumnIndex(columnName))

fun Cursor.getBlobOrNull(columnName: String) = getBlobOrNull(getColumnIndex(columnName))

fun Cursor.getDoubleOrNull(columnName: String) = getDoubleOrNull(getColumnIndex(columnName))

fun Cursor.getIntOrNull(columnName: String) = getIntOrNull(getColumnIndex(columnName))




