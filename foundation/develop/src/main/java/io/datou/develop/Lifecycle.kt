package io.datou.develop

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch

class LifecycleDestroyedScope {
    inline fun onDestroyed(
        crossinline onDestroyedEffect: () -> Unit
    ) = object : LifecycleDestroyed {
        override fun onDestroyed() {
            onDestroyedEffect()
        }
    }
}

interface LifecycleDestroyed {
    fun onDestroyed()
}

inline fun LifecycleOwner.withLifecycleDestroyed(
    effect: LifecycleDestroyedScope.() -> LifecycleDestroyed
) {

    if (lifecycle.currentState == Lifecycle.State.DESTROYED) {
        return
    }
    val callbackResult = LifecycleDestroyedScope().effect()
    lifecycle.addObserver(object : DefaultLifecycleObserver {
        override fun onDestroy(owner: LifecycleOwner) {
            callbackResult.onDestroyed()
        }
    })
}

fun LifecycleOwner.launchOnceWhenResumed(block: () -> Unit): Job {
    return if (lifecycle.currentState == Lifecycle.State.RESUMED) {
        lifecycleScope.launch { block()}
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