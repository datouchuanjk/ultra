package io.datou.develop

import android.content.Context
import android.graphics.Color
import android.view.Window
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.window.DialogWindowProvider
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

@Composable
fun findWindow() = (LocalView.current.parent as? DialogWindowProvider)
    ?.window
    ?: LocalView.current.context.findWindow()

fun Context.findWindow(): Window? = findActivity()?.window

fun Window.updateAttributes(block: WindowManager.LayoutParams.() -> Unit) {
    val attributes = this.attributes
    attributes.block()
    this.attributes = attributes
}

fun ComponentActivity.enableFullScreen(
    statusBarStyle: SystemBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT),
    navigationBarStyle: SystemBarStyle =SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT),
) {
    enableEdgeToEdge(
        statusBarStyle = statusBarStyle,
        navigationBarStyle = navigationBarStyle,
    )
    WindowCompat.getInsetsController(window, window.decorView)
        .run {
            hide(WindowInsetsCompat.Type.systemBars())
            systemBarsBehavior = BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
}



