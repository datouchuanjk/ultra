package io.datou.develop

import android.os.Build
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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

fun Modifier.onClick(enabled: Boolean = true, onClick: () -> Unit) = composed {
    if (enabled) {
        clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            onClick = onClick
        )
    } else {
        this
    }
}

fun Modifier.imePaddingCompat() = composed {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
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
                bottom =  LocalDensity.current.run {
                    keyboardHeight.toDp()
                })
        )
    }
}