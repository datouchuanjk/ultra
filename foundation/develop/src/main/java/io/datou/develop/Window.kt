package io.datou.develop

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.graphics.PixelFormat
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.Window
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresPermission
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.window.DialogWindowProvider
import androidx.core.content.getSystemService
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

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

fun Activity.addSecureFlag() {
    window.addFlags(WindowManager.LayoutParams.FLAG_SECURE)
}

fun ComponentActivity.enableFullScreen() {
    WindowCompat.setDecorFitsSystemWindows(window, false)
    val controller = WindowCompat.getInsetsController(window, window.decorView)
    controller.hide(WindowInsetsCompat.Type.systemBars())
    controller.systemBarsBehavior = BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
}

@RequiresPermission(Manifest.permission.SYSTEM_ALERT_WINDOW)
fun addViewToWindow(
    width: Int = WindowManager.LayoutParams.MATCH_PARENT,
    height: Int = WindowManager.LayoutParams.WRAP_CONTENT,
    gravity: Int = Gravity.TOP,
    content: @Composable () -> Unit
) {
    val wm = App.getSystemService<WindowManager>() ?: return
    val handler = Handler(Looper.getMainLooper())
    val params = WindowManager.LayoutParams(
        width,
        height,
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            WindowManager.LayoutParams.TYPE_SYSTEM_ALERT
        },
        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
        PixelFormat.TRANSLUCENT
    )
    params.gravity = gravity
    val composeView = ComposeView(App).apply {
        setContent {
            content()
        }
    }

    var lifecycleOwner: ComposeViewLifecycleOwner? = ComposeViewLifecycleOwner().also {
        it.onCreate()
        it.attachToDecorView(composeView)
    }
    val closeBlock = {
        if (composeView.isAttachedToWindow) {
            wm.removeView(composeView)
        }
        lifecycleOwner?.onPause()
        lifecycleOwner?.onStop()
        lifecycleOwner?.onDestroy()
        lifecycleOwner = null
    }
    wm.addView(composeView, params)
    lifecycleOwner?.onStart()
    lifecycleOwner?.onResume()
    composeView.setOnClickListener {
        closeBlock()
    }
    handler.postDelayed({
        closeBlock()
    }, 2000)
}
