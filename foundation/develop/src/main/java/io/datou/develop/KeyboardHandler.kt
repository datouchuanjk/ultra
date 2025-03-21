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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.graphics.drawable.toDrawable

fun ComponentActivity.setOnKeyboardHeightListener(
    view: View = window.decorView,
    useCompat: Boolean = true,
    listener: (Int) -> Unit
) {
    bindLifecycle {
        var observer: KeyboardHeightObserver? = null
        var keyboardHeight: Int? = null
        val handle: (Int) -> Unit = {
            if (keyboardHeight != it) {
                listener(it)
                keyboardHeight = it
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && useCompat) {
            ViewCompat.setOnApplyWindowInsetsListener(view) { _, insets ->
                val ime = insets.getInsets(WindowInsetsCompat.Type.ime())
                val imeIsVisible = insets.isVisible(WindowInsetsCompat.Type.ime())
                val navigationBars = insets.getInsets(WindowInsetsCompat.Type.navigationBars())
                val newKeyboardHeight = if (imeIsVisible) {
                    ime.bottom - navigationBars.bottom
                } else {
                    0
                }
                handle(newKeyboardHeight)
                insets
            }
        } else {
            observer = KeyboardHeightObserver(activity = this@setOnKeyboardHeightListener) {
                handle(it)
            }
        }
        onDestroy {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                ViewCompat.setOnApplyWindowInsetsListener(window.decorView, null)
            } else {
                observer?.onDispose()
            }
        }
    }
}

fun Modifier.imePaddingCompat(useCompat: Boolean = true) = composed {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && useCompat) {
        then(
            Modifier.imePadding()
        )
    } else {
        var keyboardHeight by remember {
            mutableIntStateOf(0)
        }
        val activity = LocalContext.current.findActivity()
        var listener: KeyboardHeightObserver? = remember { null }
        DisposableEffect(Unit) {
            activity?.let {
                listener = KeyboardHeightObserver(it) { height ->
                    keyboardHeight = height
                }
            }
            onDispose {
                listener?.onDispose()
            }
        }
        then(
            Modifier.padding(
                bottom = LocalDensity.current.run {
                    keyboardHeight.toDp()
                })
        )
    }
}

internal class KeyboardHeightObserver(
    activity: Activity,
    private val listener: (Int) -> Unit
) : PopupWindow(activity), OnGlobalLayoutListener {
    private val rootView = View(activity)
    private var maxHeight = 0

    init {
        rootView.viewTreeObserver.addOnGlobalLayoutListener(this)
        contentView = rootView
        setBackgroundDrawable(0.toDrawable())
        width = 0
        height = ViewGroup.LayoutParams.MATCH_PARENT
        @Suppress("DEPRECATION")
        softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
        inputMethodMode = INPUT_METHOD_NEEDED
        if (!isShowing) {
            activity.window.decorView.apply {
                post {
                    showAtLocation(this, Gravity.NO_GRAVITY, 0, 0)
                    val rect = Rect()
                    rootView.getWindowVisibleDisplayFrame(rect)
                    maxHeight = rect.bottom
                }
            }
        }
    }

    fun onDispose() {
        rootView.viewTreeObserver.removeOnGlobalLayoutListener(this)
        dismiss()
    }

    override fun onGlobalLayout() {
        val rect = Rect()
        rootView.getWindowVisibleDisplayFrame(rect)
        if (maxHeight == 0) {
            maxHeight = rect.bottom
        }
        val keyboardHeight = maxHeight - rect.bottom
        if (keyboardHeight > maxHeight / 3) {
            listener(keyboardHeight)
        } else {
            listener(0)
        }
    }
}