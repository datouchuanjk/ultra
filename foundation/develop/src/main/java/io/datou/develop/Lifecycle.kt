package io.datou.develop

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch

class LifecycleDisposableScope {
    inline fun onDispose(
        crossinline onDisposeEffect: () -> Unit
    ) = object : LifecycleDisposable {
        override fun onDispose() {
            onDisposeEffect()
        }
    }
}

interface LifecycleDisposable {
    fun onDispose()
}


inline fun LifecycleOwner.withLifecycleDisposable(
    effect: LifecycleDisposableScope.() -> LifecycleDisposable
) {
    if (lifecycle.currentState == Lifecycle.State.DESTROYED) {
        return
    }
    val callbackResult = LifecycleDisposableScope().effect()
    lifecycle.addObserver(object : DefaultLifecycleObserver {
        override fun onDestroy(owner: LifecycleOwner) {
            callbackResult.onDispose()
        }
    })
}

fun LifecycleOwner.launchOnceWhenResumed(block: () -> Unit): Job? {
    return if (lifecycle.currentState == Lifecycle.State.RESUMED) {
        block()
        null
    } else {
        lifecycleScope.launch {
            lifecycle.currentStateFlow
                .filter { it == Lifecycle.State.RESUMED }
                .take(1)
                .collect {
                    block()
                }
        }
    }
}