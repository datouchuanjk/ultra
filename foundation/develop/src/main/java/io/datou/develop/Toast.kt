package io.datou.develop

import android.Manifest
import android.content.DialogInterface
import android.graphics.PixelFormat
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.RequiresPermission
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.core.content.getSystemService
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.lifecycle.setViewTreeViewModelStoreOwner
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryController
import androidx.savedstate.SavedStateRegistryOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner

fun toast(message: String?, duration: Int = Toast.LENGTH_SHORT) {
    message ?: return
    if (Looper.getMainLooper().thread == Thread.currentThread()) {
        Toast.makeText(InstanceApp, message, duration).show()
    } else {
        Handler(Looper.getMainLooper()).post {
            Toast.makeText(InstanceApp, message, duration).show()
        }
    }
}

@RequiresPermission(Manifest.permission.SYSTEM_ALERT_WINDOW)
fun alertToast(
    width: Int = WindowManager.LayoutParams.MATCH_PARENT,
    height: Int = WindowManager.LayoutParams.WRAP_CONTENT,
    gravity: Int = Gravity.TOP,
    content: @Composable DialogInterface.() -> Unit
) {
    val wm = InstanceApp.getSystemService<WindowManager>() ?: return
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
    val composeView = ComposeView(InstanceApp)
    var dialogLifecycleOwner: DialogLifecycleOwner? = DialogLifecycleOwner(composeView)
    dialogLifecycleOwner?.create {
        wm.addView(composeView, params)
    }
    val closeBlock = {
        dialogLifecycleOwner?.destroy {
            if (composeView.isAttachedToWindow) {
                wm.removeView(composeView)
            }
        }
        dialogLifecycleOwner = null
    }
    dialogLifecycleOwner?.resume()
    composeView.setOnClickListener {
        closeBlock()
    }
    composeView.setContent {
        content(object : DialogInterface {
            override fun cancel() {
                closeBlock()
            }

            override fun dismiss() {
                closeBlock()
            }
        }
        )
    }
}

private class DialogLifecycleOwner(
    private val rootView: View
) : LifecycleOwner,
    ViewModelStoreOwner,
    SavedStateRegistryOwner {

    private val _lifecycleRegistry: LifecycleRegistry = LifecycleRegistry(this)
    private val _savedStateRegistryController = SavedStateRegistryController.create(this)
    private val _viewModelStore = ViewModelStore()

    override val lifecycle: Lifecycle
        get() = _lifecycleRegistry
    override val savedStateRegistry: SavedStateRegistry
        get() = _savedStateRegistryController.savedStateRegistry
    override val viewModelStore: ViewModelStore
        get() = _viewModelStore


    fun create(action: (View) -> Unit) {
        _savedStateRegistryController.performRestore(null)
        _lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
        rootView.also {
            it.setViewTreeViewModelStoreOwner(this)
            it.setViewTreeLifecycleOwner(this)
            it.setViewTreeSavedStateRegistryOwner(this)
        }.let(action)
    }

    fun resume() {
        _lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START)
        _lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
    }


    fun destroy(action: (View) -> Unit) {
        action(rootView)
        _lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        _lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_STOP)
        _lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        _viewModelStore.clear()
    }

}
