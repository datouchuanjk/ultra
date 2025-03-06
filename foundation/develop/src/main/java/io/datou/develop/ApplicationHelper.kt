package io.datou.develop

import android.app.Application
import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.take

lateinit var App: Application
    private set

val AppCurrentStateFlow = ProcessLifecycleOwner.get().lifecycle.currentStateFlow

suspend fun launchWhenAppResumed(block: () -> Unit) {
    if (AppCurrentStateFlow.value == Lifecycle.State.RESUMED) {
        block()
    } else {
        AppCurrentStateFlow
            .filter { it == Lifecycle.State.RESUMED }
            .take(1)
            .collectLatest {
                block()
            }
    }
}

internal class ApplicationProvider : ContentProvider() {
    override fun onCreate(): Boolean {
        val currentContext = context ?: return false
        if (currentContext is Application) {
            App = currentContext
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