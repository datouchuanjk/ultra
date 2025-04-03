package io.datou.develop

import android.app.Activity
import android.graphics.Rect
import android.os.Build
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.view.WindowManager
import android.widget.PopupWindow
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.platform.LocalDensity
import androidx.core.graphics.drawable.toDrawable
import androidx.lifecycle.DEFAULT_ARGS_KEY
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

fun ComponentActivity.addOnKeyboardListener(
    onHeightChange: (Int) -> Unit
) {
    withLifecycleDisposable {
        val keyboardHandler = KeyboardHandler(
            this@addOnKeyboardListener,
            onHeightChange
        )
        onDispose {
            keyboardHandler.onDispose()
        }
    }
}

@Composable
fun rememberKeyboardHeight(): Int {
    var value by remember {
        mutableIntStateOf(0)
    }
    val activity = findActivity()
    LaunchedEffect(Unit) {
        if (activity is ComponentActivity) {
            activity.addOnKeyboardListener {
                value = it
            }
        }
    }
    return value
}

internal class KeyboardHandler(
    activity: Activity,
    private val onHeightChange: (Int) -> Unit
) : PopupWindow(activity), OnGlobalLayoutListener {
    private val _rootView = View(activity)
    private var _maxHeight = 0
    private var _keyboardHeight: Int = 0

    init {
        _rootView.viewTreeObserver.addOnGlobalLayoutListener(this)
        contentView = _rootView
        setBackgroundDrawable(0.toDrawable())
        width = 0
        height = ViewGroup.LayoutParams.MATCH_PARENT
        softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
        inputMethodMode = INPUT_METHOD_NEEDED
        if (!isShowing) {
            activity.window.decorView.apply {
                post {
                    showAtLocation(this, Gravity.NO_GRAVITY, 0, 0)
                    val rect = Rect()
                    rootView.getWindowVisibleDisplayFrame(rect)
                    _maxHeight = rect.bottom
                }
            }
        }
    }

    fun onDispose() {
        _rootView.viewTreeObserver.removeOnGlobalLayoutListener(this)
        dismiss()
    }

    override fun onGlobalLayout() {
        val rect = Rect()
        _rootView.getWindowVisibleDisplayFrame(rect)
        if (_maxHeight == 0) {
            _maxHeight = rect.bottom
        }
        (_maxHeight - rect.bottom).run {
            if (this > _maxHeight / 3) {
                this
            } else {
                0
            }
        }.run {
            if (this != _keyboardHeight) {
                _keyboardHeight = this
                onHeightChange(this)
            }
        }
    }
}