package io.standard.tools

import androidx.activity.ComponentActivity
import androidx.activity.addCallback
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner

fun ComponentActivity.confirmExit(
    doubleClickInterval: Long = 1500,
    action: (Int) -> Unit = {
        if (it == 1) {
            toast("Click again to exit application")
        } else {
            killAllActivities()
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


