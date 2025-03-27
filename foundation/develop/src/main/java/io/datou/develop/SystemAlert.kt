package io.datou.develop

import android.Manifest
import android.content.DialogInterface
import android.graphics.PixelFormat
import android.os.Build
import android.view.Gravity
import android.view.WindowManager
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

@RequiresPermission(Manifest.permission.SYSTEM_ALERT_WINDOW)
fun systemAlert(
    width: Int = WindowManager.LayoutParams.MATCH_PARENT,
    height: Int = WindowManager.LayoutParams.WRAP_CONTENT,
    flag: Int = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
    format: Int = PixelFormat.TRANSLUCENT,
    gravity: Int = Gravity.TOP,
    content: @Composable DialogInterface.() -> Unit
): DialogInterface = SystemAlert(
    width = width,
    height = height,
    flag = flag,
    format = format,
    gravity = gravity,
    content = content
).apply {
    create()
    resume()
}

private class SystemAlert(
    width: Int = WindowManager.LayoutParams.MATCH_PARENT,
    height: Int = WindowManager.LayoutParams.WRAP_CONTENT,
    flag: Int = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
    format: Int = PixelFormat.TRANSLUCENT,
    gravity: Int = Gravity.TOP,
    content: @Composable DialogInterface.() -> Unit
) : LifecycleOwner,
    ViewModelStoreOwner,
    SavedStateRegistryOwner, DialogInterface {
    private val _lifecycleRegistry: LifecycleRegistry = LifecycleRegistry(this)
    private val _savedStateRegistryController = SavedStateRegistryController.create(this)
    private val _viewModelStore = ViewModelStore()
    override val lifecycle: Lifecycle
        get() = _lifecycleRegistry
    override val savedStateRegistry: SavedStateRegistry
        get() = _savedStateRegistryController.savedStateRegistry
    override val viewModelStore: ViewModelStore
        get() = _viewModelStore
    private val _windowManager = Instance.getSystemService<WindowManager>()
    private val _layoutParams = WindowManager.LayoutParams(
        width,
        height,
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            WindowManager.LayoutParams.TYPE_SYSTEM_ALERT
        },
        flag,
        format
    ).also {
        it.gravity = gravity
    }
    private val _composeView = ComposeView(Instance)
        .apply {
            setContent { content() }
        }

    fun create() {
        _savedStateRegistryController.performRestore(null)
        _lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
        _composeView.setViewTreeViewModelStoreOwner(this)
        _composeView.setViewTreeLifecycleOwner(this)
        _composeView.setViewTreeSavedStateRegistryOwner(this)
        _windowManager?.addView(_composeView, _layoutParams)
    }

    fun resume() {
        _lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START)
        _lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
    }

    fun destroy() {
        if (_composeView.isAttachedToWindow) {
            _windowManager?.removeView(_composeView)
        }
        _lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        _lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_STOP)
        _lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        _viewModelStore.clear()
        _composeView.setContent {}
    }

    override fun cancel() {
        destroy()
    }

    override fun dismiss() {
        destroy()
    }
}