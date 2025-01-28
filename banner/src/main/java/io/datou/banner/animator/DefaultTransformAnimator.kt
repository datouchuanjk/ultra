package io.datou.banner.animator

import androidx.compose.ui.graphics.GraphicsLayerScope
import androidx.compose.ui.util.lerp

 class DefaultTransformAnimator : BannerTransformAnimator {
    override fun GraphicsLayerScope.transform(fraction: Float) {
        val value = lerp(0.8f, 1f, fraction)
        scaleY = value
        alpha = value
    }
}