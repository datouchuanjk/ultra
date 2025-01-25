package io.standard.composable.banner.animator

import androidx.compose.ui.graphics.GraphicsLayerScope

fun interface BannerTransformAnimator {
    fun GraphicsLayerScope.transform(fraction: Float)
}

