package io.standard.tools

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.view.Window
import android.view.WindowManager
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.window.DialogWindowProvider

@Composable
fun findWindow() = (LocalView.current.parent as? DialogWindowProvider)?.window
    ?: LocalView.current.context.findWindow()

 fun Context.findWindow(): Window? = when (this) {
    is Activity -> window
    is ContextWrapper -> baseContext.findWindow()
    else -> null
}

fun Window.updateAttributes(block: WindowManager.LayoutParams.() -> Unit) {
    val attributes = this.attributes
    attributes.block()
    this.attributes = attributes
}

