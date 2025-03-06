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

fun Application.sakura() {
    App = this
}

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
