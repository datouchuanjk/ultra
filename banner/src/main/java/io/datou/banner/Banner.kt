package io.datou.banner

import android.util.Log
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.TargetedFlingBehavior
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.datou.banner.animator.BannerTransformAnimator
import io.datou.banner.animator.DefaultTransformAnimator
import kotlin.math.absoluteValue

@Composable
fun Banner(
    state: BannerState,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(30.dp),
    pageSize: PageSize = PageSize.Fill,
    beyondViewportPageCount: Int = PagerDefaults.BeyondViewportPageCount,
    pageSpacing: Dp = 15.dp,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    flingBehavior: TargetedFlingBehavior = PagerDefaults.flingBehavior(state = state.pagerState),
    userScrollEnabled: Boolean = true,
    reverseLayout: Boolean = false,
    key: ((index: Int) -> Any)? = null,
    pageNestedScrollConnection: NestedScrollConnection = PagerDefaults.pageNestedScrollConnection(
        state.pagerState,
        Orientation.Horizontal
    ),
    snapPosition: SnapPosition = SnapPosition.Start,
    transformAnimator: BannerTransformAnimator = DefaultTransformAnimator(),
    pageContent: @Composable PagerScope.(page: Int) -> Unit
) {
    HorizontalPager(
        state = state.pagerState,
        modifier = modifier,
        contentPadding = contentPadding,
        pageSize = pageSize,
        beyondViewportPageCount = beyondViewportPageCount,
        pageSpacing = pageSpacing,
        verticalAlignment = verticalAlignment,
        flingBehavior = flingBehavior,
        userScrollEnabled = userScrollEnabled,
        reverseLayout = reverseLayout,
        key = key,
        pageNestedScrollConnection = pageNestedScrollConnection,
        snapPosition = snapPosition,
    ) { page ->
        val actualPage = state.calculateActualPage(page)
        Log.e("1234","page=${page} actualPage=${actualPage}")
        Box(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    (state.currentPage - actualPage + state.currentPageOffsetFraction)
                        .run {
                            1f - absoluteValue.coerceIn(0f, 1f)
                        }
                        .let {
                            transformAnimator.apply {
                                transform(it)
                            }
                        }
                }, contentAlignment = Alignment.Center
        ) {
            pageContent(actualPage)
        }
    }
}