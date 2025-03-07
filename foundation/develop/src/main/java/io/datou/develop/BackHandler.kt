package io.datou.develop

import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver

fun ComponentActivity.confirmExit(
    doubleClickInterval: Long = 1500,
    action: OnBackPressedCallback.(Boolean) -> Unit = {
        if (it) {
            finish()
        } else {
            toast("Click again to exit App")
        }
    }
) {
    var lastClickTime = 0L
    onBackPressedDispatcher.addCallback(this) {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastClickTime < doubleClickInterval) {
            action(true)
        } else {
            action(false)
            lastClickTime = currentTime
        }
    }
}


