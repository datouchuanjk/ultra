package io.datou.banner.animator

import androidx.compose.ui.graphics.GraphicsLayerScope

class FlipTransformAnimator : BannerTransformAnimator {
    override fun GraphicsLayerScope.transform(fraction: Float) {
        val degrees = (1 - fraction) * 180f
        rotationZ = degrees
    }
}