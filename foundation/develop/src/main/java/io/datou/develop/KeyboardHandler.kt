package io.datou.develop

import android.app.Activity
import android.graphics.Rect
import android.os.Build
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.view.WindowManager
import android.widget.PopupWindow
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.platform.LocalDensity
import androidx.core.view.ViewCompat
import androidx.core.graphics.drawable.toDrawable

fun ComponentActivity.setOnKeyboardListenerCompat(
    view: View = window.decorView,
    useCompat: Boolean = true,
    onHeightChange: (Int) -> Unit
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R
        && useCompat
    ) {
        withLifecycleDisposable {
            ViewCompat.setOnApplyWindowInsetsListener(view) { _, insets ->
                if (insets.isImeVisible) {
                    insets.getImeInsets().bottom - insets.getNavigationBarsInsets().bottom
                } else {
                    0
                }.let(onHeightChange)
                insets
            }
            onDispose {
                ViewCompat.setOnApplyWindowInsetsListener(view, null)
            }
        }
    } else {
        val activity = this
        withLifecycleDisposable {
            val keyboardHandler = KeyboardHandler(
                activity,
                onHeightChange
            )
            onDispose {
                keyboardHandler.onDispose()
            }
        }
    }
}

fun Modifier.imePaddingCompat(useCompat: Boolean = true) = composed {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && useCompat) {
        then(Modifier.imePadding())
    } else {
        var keyboardHeight by remember {
            mutableIntStateOf(0)
        }
        val activity = findActivity()
        DisposableEffect(Unit) {
            val listener = activity?.let {
                KeyboardHandler(it) { height ->
                    keyboardHeight = height
                }
            }
            onDispose {
                listener?.onDispose()
            }
        }
        then(
            Modifier.padding(bottom = LocalDensity.current.run { keyboardHeight.toDp() })
        )
    }
}

internal class KeyboardHandler(
    activity: Activity,
    private val onHeightChange: (Int) -> Unit
) : PopupWindow(activity), OnGlobalLayoutListener {
    private val _rootView = View(activity)
    private var _maxHeight = 0

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
        val keyboardHeight = _maxHeight - rect.bottom
        if (keyboardHeight > _maxHeight / 3) {
            onHeightChange(keyboardHeight)
        } else {
            onHeightChange(0)
        }
    }
}