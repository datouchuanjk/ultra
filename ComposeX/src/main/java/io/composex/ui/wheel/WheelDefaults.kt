package io.composex.ui.wheel

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.GraphicsLayerScope
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp

object WheelDefaults {
    val itemHeight = 47.dp
    const val VISIBLE_COUNT = 5
    val draw: ContentDrawScope.(Rect) -> Unit = {
        drawLine(
            color = Color.Black,
            strokeWidth = 1f,
            start = Offset(it.left, it.top),
            end = Offset(it.right, it.top)
        )
        drawLine(
            color = Color.Black,
            strokeWidth = 1f,
            start = Offset(it.left, it.bottom),
            end = Offset(it.right, it.bottom)
        )
    }

    val animator: GraphicsLayerScope.(Float) -> Unit = {
        rotationX = lerp(0f, 45f, it)
        scaleX = lerp(0.7f, 1f, 1f - it)
        scaleY = lerp(0.7f, 1f, 1f - it)
        alpha = lerp(0.5f, 1f, 1f - it)
        cameraDistance = 8f * density
    }
}