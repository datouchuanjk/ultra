package io.standard.composable.picker

import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun RowScope.Wheel(
    modifier: Modifier = Modifier,
    coverBackgroundColor: Color = Color.White,
    state: LazyListState,
    itemCount: Int,
    itemHeight: Dp = 47.dp,
    visibleCount: Int = 5,
    flingBehavior: FlingBehavior = rememberSnapFlingBehavior(state),
    drawCover: ContentDrawScope.() -> Unit = {
        drawWheelBackground(coverBackgroundColor)
        drawContent()
        drawWheelCover(itemHeight, coverBackgroundColor)
    },
    content: @Composable (Int) -> Unit
) {
    if (visibleCount % 2 == 0) {
        error("visibleCount must be odd number")
    }
    val maxHeight = remember {
        itemHeight * visibleCount
    }
    val contentPadding = remember {
        PaddingValues(vertical = (maxHeight - itemHeight) / 2)
    }
    LazyColumn(
        userScrollEnabled = itemCount > 0,
        contentPadding = contentPadding,
        modifier = modifier
            .height(maxHeight)
            .weight(1f)
            .drawWithContent {
                drawCover()
            },

        state = state,
        horizontalAlignment = Alignment.CenterHorizontally,
        flingBehavior = flingBehavior
    ) {
        items(itemCount, key = {
            it
        }) {
            Box(
                modifier = Modifier
                    .fillParentMaxWidth()
                    .height(itemHeight),
                contentAlignment = Alignment.Center
            ) {
                content(it)
            }
        }
    }
}

internal fun DrawScope.drawWheelCover(itemHeight: Dp = 47.dp, color: Color) {
    val centerY = size.height / 2
    val itemHeightPx = itemHeight.toPx()
    drawLine(
        color = Color.Black,
        strokeWidth = 1f,
        start = Offset(0f, centerY - itemHeightPx / 2),
        end = Offset(size.width, centerY - itemHeightPx / 2)
    )
    drawLine(
        color = Color.Black,
        strokeWidth = 1f,
        start = Offset(0f, centerY + itemHeightPx / 2),
        end = Offset(size.width, centerY + itemHeightPx / 2)
    )
    drawRect(
        brush = Brush.verticalGradient(
            colors = listOf(color, color.copy(0f)),
        ),
        topLeft = Offset(0f, 0f),
        size = Size(size.width, centerY - itemHeightPx / 2)
    )
    drawRect(
        brush = Brush.verticalGradient(
            colors = listOf(color.copy(0f), color),
        ),
        topLeft = Offset(0f, centerY + itemHeightPx / 2),
        size = Size(size.width, centerY - itemHeightPx / 2)
    )
}

internal fun DrawScope.drawWheelBackground(color: Color) {
    drawRect(
        color = color,
        topLeft = Offset(0f, 0f),
        size = Size(size.width, size.height)
    )
}
