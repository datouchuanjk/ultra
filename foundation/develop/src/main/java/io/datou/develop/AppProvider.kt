package io.datou.develop

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri

internal var InternalApp: Application? = null
    @SuppressLint("MissingPermission")
    set(value) {
        if (field == null && value != null) {
            field = value
            registerApplicationLifecycleStateObserver()
            if (isPermissionGranted(Manifest.permission.ACCESS_NETWORK_STATE)) {
                registerNetworkObserver()
            }
            registerActivitiesObserver()
        }
    }

val App get() = checkNotNull(InternalApp)

internal class AppProvider : ContentProvider() {
    override fun onCreate(): Boolean {
        val currentContext = context ?: return false
        if (currentContext is Application) {
            InternalApp = currentContext
        }
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor? {
        throw IllegalArgumentException("Not supported")
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        throw UnsupportedOperationException("Not supported")
    }

    override fun delete(
        uri: Uri,
        selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        throw UnsupportedOperationException("Not supported")
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        throw UnsupportedOperationException("Not supported")
    }
}