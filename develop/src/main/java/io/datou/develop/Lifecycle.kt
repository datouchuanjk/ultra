package io.datou.develop

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner

internal class DisposableEffectScope {
    inline fun onDispose(
        crossinline onDisposeEffect: () -> Unit
    ) = object : DisposableEffectResult {
        override fun dispose() {
            onDisposeEffect()
        }
    }
}

internal interface DisposableEffectResult {
    fun dispose()
}

internal inline fun LifecycleOwner.disposableEffect(
    effect: DisposableEffectScope.() -> DisposableEffectResult
) {
    if (lifecycle.currentState == Lifecycle.State.DESTROYED) {
        return
    }
    val callbackResult = DisposableEffectScope().effect()
    lifecycle.addObserver(object : DefaultLifecycleObserver {
        override fun onDestroy(owner: LifecycleOwner) {
            callbackResult.dispose()
        }
    })
}
