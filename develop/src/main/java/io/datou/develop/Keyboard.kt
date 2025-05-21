package io.datou.develop

import android.app.Activity
import android.graphics.Rect
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.view.WindowManager
import android.widget.PopupWindow
import androidx.activity.ComponentActivity
import androidx.core.graphics.drawable.toDrawable
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

val LifecycleOwner.KeyboardHeightFlow: StateFlow<Int>
    get() {
        val state = MutableStateFlow(0)
        disposableEffect {
            val listener = KeyboardHeightListener(TopActivityOrNull) {
                state.value = it
            }
            onDispose {
                listener.onDispose()
            }
        }
        return state.asStateFlow()
    }

internal class KeyboardHeightListener(
    private val activity: Activity?,
    private val onHeightChange: (Int) -> Unit
) : PopupWindow(activity), OnGlobalLayoutListener {
    private val _rootView = View(activity)
    private var _maxHeight = 0
    private var _keyboardHeight: Int = 0

    init {
        _rootView.viewTreeObserver.addOnGlobalLayoutListener(this@KeyboardHeightListener)
        contentView = _rootView
        setBackgroundDrawable(0.toDrawable())
        width = 0
        height = ViewGroup.LayoutParams.MATCH_PARENT
        softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
        inputMethodMode = INPUT_METHOD_NEEDED
        if (!isShowing) {
            TopActivityOrNull?.window?.decorView?.apply {
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
        _rootView.viewTreeObserver.removeOnGlobalLayoutListener(this@KeyboardHeightListener)
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