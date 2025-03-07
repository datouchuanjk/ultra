package io.datou.develop

import android.app.Application
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.take

fun Application.develop() {
    App = this
    ActivityHelper.registerActivityLifecycleCallbacks()
}

lateinit var App: Application
    private set

val AppLifecycleCurrentStateFlow = ProcessLifecycleOwner.get().lifecycle.currentStateFlow

val AppLifecycleScope = ProcessLifecycleOwner.get().lifecycleScope

suspend fun launchWhenAppResumed(block: () -> Unit) {
    if (AppLifecycleCurrentStateFlow.value == Lifecycle.State.RESUMED) {
        block()
    } else {
        AppLifecycleCurrentStateFlow
            .filter { it == Lifecycle.State.RESUMED }
            .take(1)
            .collectLatest {
                block()
            }
    }
}
