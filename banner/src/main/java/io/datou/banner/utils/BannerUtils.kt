package io.datou.banner.utils

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import io.datou.banner.BannerState
import kotlinx.coroutines.delay

@Composable
fun BannerState.withAutoPlay(
    autoPlayInterval: Long = 3000,
    animationSpec: AnimationSpec<Float> = tween(500)
): BannerState {
    val isDragged by interactionSource.collectIsDraggedAsState()
    if (!isDragged) {
        LaunchedEffect(key1 = settledPage) {
            if (pagerState.pageCount > 0) {
                delay(autoPlayInterval)
                val next = (pagerState.currentPage + 1).mod(pagerState.pageCount)
                animateScrollToPage(
                    page = next, animationSpec = animationSpec
                )
            }
        }
    }
    return this
}