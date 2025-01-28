package io.datou.banner

import androidx.annotation.FloatRange
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember

@Composable
fun rememberInfiniteBannerState(
    initialPage: Int = 0,
    @FloatRange(from = -0.5, to = 0.5)
    initialPageOffsetFraction: Float = 0f,
    pageCount: () -> Int
): BannerState {
    val pagerState = rememberPagerState(
        initialPage = (Int.MAX_VALUE / 2).run {
            this - this % pageCount() + initialPage
        },
        initialPageOffsetFraction = initialPageOffsetFraction,
        pageCount = {
            Int.MAX_VALUE
        }
    )
    return remember(pagerState) {
        InfiniteBannerState(
            pagerState = pagerState,
            actualCount = pageCount()
        )
    }
}

internal class InfiniteBannerState(
    pagerState: PagerState,
    private val actualCount: Int,
) : MutableBannerState(pagerState = pagerState) {
    override val pageCount: Int get() = actualCount
    override val currentPage: Int by derivedStateOf { pagerState.currentPage % actualCount }
    override val settledPage: Int by derivedStateOf { pagerState.settledPage % actualCount }
    override fun calculateActualPage(page: Int): Int {
        return page % actualCount
    }
}