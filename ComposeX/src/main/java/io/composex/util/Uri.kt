package io.composex.util

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import java.io.InputStream
import java.io.OutputStream

fun Uri.insertTo(context: Context, values: ContentValues.() -> Unit): Uri? {
    return context.contentResolver.insert(this, ContentValues().apply(values))
}

fun Uri.openInputStream(context: Context): InputStream? {
    return context.contentResolver.openInputStream(this)
}

fun Uri.openOutputStream(context: Context): OutputStream? {
    return context.contentResolver.openOutputStream(this)
}

fun Uri.delete(context: Context): Int {
    return context.contentResolver.delete(this, null, null)
}

fun Uri.update(context: Context, values: ContentValues.() -> Unit): Int {
    return context.contentResolver.update(this, ContentValues().apply(values), null, null)
}