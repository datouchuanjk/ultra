package io.datou.develop

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch

class DisposableLifecycleScope {
    inline fun onDestroy(
        crossinline onDisposeEffect: () -> Unit
    ): DisposableLifecycleResult = object : DisposableLifecycleResult {
        override fun onDestroy() {
            onDisposeEffect()
        }
    }
}

interface DisposableLifecycleResult {
    fun onDestroy()
}

inline fun LifecycleOwner.bindLifecycle(
    effect: DisposableLifecycleScope.() -> DisposableLifecycleResult
) {
    if (lifecycle.currentState == Lifecycle.State.DESTROYED) {
        return
    }
    val callbackResult = DisposableLifecycleScope().effect()
    lifecycle.addObserver(object : DefaultLifecycleObserver {
        override fun onDestroy(owner: LifecycleOwner) {
            callbackResult.onDestroy()
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