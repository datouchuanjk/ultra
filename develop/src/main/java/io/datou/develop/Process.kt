package io.datou.develop

import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.ProcessLifecycleOwner
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

private val InternalProcessObserver = MutableStateFlow(
    ProcessLifecycleOwner
        .get()
        .lifecycle
        .currentState
)

val ProcessObserver = InternalProcessObserver.asStateFlow()

internal fun registerProcessObserver() {
    ProcessLifecycleOwner.get().lifecycle.addObserver(
        LifecycleEventObserver { _, event ->
            InternalProcessObserver.value = event.targetState
        }
    )
}