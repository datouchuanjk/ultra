package io.composex.ui.wheel

import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.GraphicsLayerScope
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.absoluteValue
import  androidx.compose.ui.util.lerp

@Composable
fun RowScope.Wheel(
    modifier: Modifier = Modifier,
    state: LazyListState,
    itemCount: Int,
    itemHeight: Dp = 47.dp,
    visibleCount: Int = 5,
    flingBehavior: FlingBehavior = rememberSnapFlingBehavior(state),
    draw: ContentDrawScope.(Rect) -> Unit = {
        drawLine(
            color = Color.Black,
            strokeWidth = 1f,
            start = Offset(it.left, it.top),
            end = Offset(it.right, it.top)
        )
        drawLine(
            color = Color.Black,
            strokeWidth = 1f,
            start = Offset(it.left, it.bottom),
            end = Offset(it.right, it.bottom)
        )
    },
    animator: GraphicsLayerScope.(Float) -> Unit = {
        alpha = 1 - it
        scaleY = lerp(0.7f, 1.0f, 1 - it)
    },
    content: @Composable (Int) -> Unit
) {
    if (visibleCount % 2 == 0) {
        error("visibleCount must be odd number")
    }
    LazyColumn(
        userScrollEnabled = itemCount > 0,
        contentPadding = PaddingValues(vertical = (itemHeight * (visibleCount - 1)) / 2),
        modifier = modifier
            .height(itemHeight * visibleCount)
            .weight(1f)
            .drawWithContent {
                drawContent()
                val rect = Rect(
                    left = 0f,
                    top = size.height / 2f - itemHeight.toPx() / 2f,
                    right = size.width,
                    bottom = size.height / 2f + itemHeight.toPx() / 2f,
                )
                draw(rect)
            },
        state = state,
        horizontalAlignment = Alignment.CenterHorizontally,
        flingBehavior = flingBehavior
    ) {
        items(itemCount, key = {
            it
        }) { index ->
            Box(
                modifier = Modifier
                    .fillParentMaxWidth()
                    .height(itemHeight)
                    .graphicsLayer {
                        state.layoutInfo.visibleItemsInfo
                            .find { it.index == index }
                            ?.let {
                                val offset = it.offset.absoluteValue
                                val total = (visibleCount / 2 + 1) * size.height
                                animator(offset / total)
                            }
                    },
                contentAlignment = Alignment.Center
            ) {
                content(index)
            }
        }
    }
}
