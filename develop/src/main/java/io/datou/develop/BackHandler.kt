package io.datou.develop

import androidx.activity.ComponentActivity
import androidx.activity.addCallback
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver

fun ComponentActivity.confirmExit(
    doubleClickInterval: Long = 1500,
    action: (Int) -> Unit = {
        if (it == 1) {
            toast("Click again to exit application")
        } else {
            finish()
        }
    }
) {
    var lastClickTime = 0L
    lifecycle.addObserver(LifecycleEventObserver { _, event ->
        if (event == Lifecycle.Event.ON_RESUME) {
            lastClickTime = 0L
        }
    })
    onBackPressedDispatcher.addCallback(this) {
        val currentTime = System.currentTimeMillis()
        when {
            lastClickTime == 0L -> {
                action(1)
                lastClickTime = currentTime
            }

            currentTime - lastClickTime < doubleClickInterval -> {
                action(2)
            }

            else -> {
                action(1)
                lastClickTime = currentTime
            }
        }
    }
}


