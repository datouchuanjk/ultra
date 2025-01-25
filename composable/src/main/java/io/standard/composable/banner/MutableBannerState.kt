package io.standard.composable.banner

import androidx.annotation.FloatRange
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember

@Composable
internal fun rememberMutableBannerState(
    initialPage: Int = 0,
    @FloatRange(from = -0.5, to = 0.5)
    initialPageOffsetFraction: Float = 0f,
    pageCount: () -> Int
): BannerState {
    val pagerState = rememberPagerState(
        initialPage = initialPage,
        initialPageOffsetFraction = initialPageOffsetFraction,
        pageCount = pageCount
    )
    return remember(pagerState) {
        MutableBannerState(pagerState)
    }
}

@Stable
internal open class MutableBannerState(override val pagerState: PagerState) : BannerState {
    override val pageCount: Int get() = pagerState.pageCount
    override val currentPage: Int by derivedStateOf { pagerState.currentPage }
    override val currentPageOffsetFraction: Float by derivedStateOf { pagerState.currentPageOffsetFraction }
    override val settledPage: Int by derivedStateOf { pagerState.settledPage }
    override val interactionSource: InteractionSource get() = pagerState.interactionSource
}