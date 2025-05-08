package io.datou.develop


import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner

internal class LifecycleDestroyedScope {
    inline fun onDestroyed(
        crossinline onDestroyedEffect: () -> Unit
    ) = object : LifecycleDestroyed {
        override fun onDestroyed() {
            onDestroyedEffect()
        }
    }
}

internal interface LifecycleDestroyed {
    fun onDestroyed()
}

internal inline fun LifecycleOwner.withLifecycleDestroyed(
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
