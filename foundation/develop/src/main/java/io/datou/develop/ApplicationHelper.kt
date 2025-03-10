package io.datou.develop

import android.app.Application
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.take

fun Application.develop() {
    ApplicationHelper.init(this)
}

val App get() = ApplicationHelper.app

val AppCurrentStateFlow get() = ApplicationHelper.currentStateFlow

val AppLifecycleScope get() = ApplicationHelper.lifecycleScope

suspend fun launchWhenAppResumed(block: () -> Unit) {
    ApplicationHelper.launchWhenResumed(block)
}

internal object ApplicationHelper {

    private var _application: Application? = null

    internal val app get() = checkNotNull(_application)

    internal fun init(application: Application) {
        _application = application
        ActivityHelper.registerActivityLifecycleCallbacks(application)
    }

    internal val currentStateFlow = ProcessLifecycleOwner.get().lifecycle.currentStateFlow

    internal val lifecycleScope = ProcessLifecycleOwner.get().lifecycleScope

    internal suspend fun launchWhenResumed(block: () -> Unit) {
        if (currentStateFlow.value == Lifecycle.State.RESUMED) {
            block()
        } else {
            currentStateFlow
                .filter { it == Lifecycle.State.RESUMED }
                .take(1)
                .collectLatest {
                    block()
                }
        }
    }
}
