package io.watermelon.banner

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.GraphicsLayerScope
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.util.lerp
import kotlin.math.absoluteValue

@Composable
fun AnimatedPageTransition(
    page: Int,
    state: PagerState,
    animator: GraphicsLayerScope.(Float) -> Unit = {
        lerp(0.85f, 1f, it).let { value ->
            scaleY = value
            alpha = value
        }
    },
    content: @Composable BoxScope.() -> Unit
) {
    val value = 1 - (state.currentPage - page + state.currentPageOffsetFraction)
        .absoluteValue.coerceIn(0f, 1f)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer {
                animator(value)
            },
        contentAlignment = Alignment.Center,
        content = content
    )
}