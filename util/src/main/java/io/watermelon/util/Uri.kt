package io.watermelon.util

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import java.io.InputStream
import java.io.OutputStream

fun Uri.insert(context: Context, values: ContentValues.() -> Unit): Uri? {
    return context.applicationContext.contentResolver.insert(this, ContentValues().apply(values))
}

fun Uri.openInputStream(context: Context): InputStream? {
    return context.applicationContext.contentResolver.openInputStream(this)
}

fun Uri.openOutputStream(context: Context): OutputStream? {
    return context.applicationContext.contentResolver.openOutputStream(this)
}

fun Uri.delete(context: Context): Int {
    return context.applicationContext.contentResolver.delete(this, null, null)
}

fun Uri.update(context: Context, values: ContentValues.() -> Unit): Int {
    return context.applicationContext.contentResolver.update(
        this,
        ContentValues().apply(values),
        null,
        null
    )
}




