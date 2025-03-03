package io.datou.develop

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.take

internal val InternalApplicationLifecycleStateObserver = MutableStateFlow(
    ProcessLifecycleOwner.get()
        .lifecycle
        .currentState
)

val ApplicationLifecycleStateObserver = InternalApplicationLifecycleStateObserver.asStateFlow()

internal fun registerApplicationLifecycleStateObserver() {
    ProcessLifecycleOwner.get().lifecycle.addObserver(
        LifecycleEventObserver { _, event ->
            InternalApplicationLifecycleStateObserver.value = event.targetState
        }
    )
}

fun launchWhenApplicationResumed(block: () -> Unit) {
    if (ApplicationLifecycleStateObserver.value == Lifecycle.State.RESUMED) {
        block()
    } else {
        ProcessLifecycleOwner.get().lifecycleScope.launchSilently {
            ApplicationLifecycleStateObserver
                .filter { it == Lifecycle.State.RESUMED }
                .take(1)
                .collectLatest {
                    block()
                }
        }.invokeOnCompletion {
            it?.printStackTrace()
        }
    }
}