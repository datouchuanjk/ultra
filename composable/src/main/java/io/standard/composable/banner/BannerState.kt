package io.standard.composable.banner

import androidx.annotation.FloatRange
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.spring
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable

@Composable
fun rememberBannerState(
    initialPage: Int = 0,
    @FloatRange(from = -0.5, to = 0.5)
    initialPageOffsetFraction: Float = 0f,
    pageCount: () -> Int
): BannerState {
    return rememberMutableBannerState(
        initialPage = initialPage,
        initialPageOffsetFraction = initialPageOffsetFraction,
        pageCount = pageCount,
    )
}

@Stable
interface BannerState {
    val pagerState: PagerState
    val pageCount: Int
    val currentPage: Int
    val currentPageOffsetFraction: Float
    val settledPage: Int
    val interactionSource: InteractionSource

    suspend fun animateScrollToPage(
        page: Int,
        @FloatRange(from = -0.5, to = 0.5) pageOffsetFraction: Float = 0f,
        animationSpec: AnimationSpec<Float> = spring()
    ) {
        pagerState.animateScrollToPage(
            page = page,
            pageOffsetFraction = pageOffsetFraction,
            animationSpec = animationSpec
        )
    }

    fun calculateActualPage(page: Int) = page
}