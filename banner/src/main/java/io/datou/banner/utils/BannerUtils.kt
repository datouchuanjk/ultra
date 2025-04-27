package io.datou.banner.utils

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import io.datou.banner.BannerState
import kotlinx.coroutines.delay

@Composable
fun BannerState.withAutoPlay(
    autoPlayInterval: Long = 3000,
    animationSpec: AnimationSpec<Float> = tween(500)
) = apply {
    if (pagerState.pageCount <= 0)
        return@apply
    val isDragged by interactionSource.collectIsDraggedAsState()
    if (!isDragged) {
        LaunchedEffect(settledPage) {
            delay(autoPlayInterval)
            val next = pagerState.currentPage
                .plus(1)
                .mod(pagerState.pageCount)
            animateScrollToPage(
                page = next,
                animationSpec = animationSpec
            )
        }
    }
}

