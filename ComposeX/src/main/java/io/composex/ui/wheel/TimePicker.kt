package io.composex.ui.wheel

import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.GraphicsLayerScope
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.unit.Dp
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter

@Composable
fun TimePicker(
    modifier: Modifier = Modifier,
    state: @Composable (Int) -> LazyListState = { rememberLazyListState() },
    itemHeight: Dp = WheelDefaults.itemHeight,
    visibleCount: Int = WheelDefaults.VISIBLE_COUNT,
    flingBehavior: @Composable (LazyListState) -> FlingBehavior = { rememberSnapFlingBehavior(it) },
    draw: ContentDrawScope.(Rect) -> Unit = WheelDefaults.draw,
    animator: GraphicsLayerScope.(Float) -> Unit = WheelDefaults.animator,
    onChange: (Triple<Int, Int, Int>) -> Unit,
    content: @Composable (IndexedValue<Int>) -> Unit,
) {
    val hourState = state(0)
    val minuteState = state(1)
    val secondState = state(2)
    val isHourDragged by hourState.interactionSource.collectIsDraggedAsState()
    val isMinuteDragged by minuteState.interactionSource.collectIsDraggedAsState()
    val isSecondDragged by secondState.interactionSource.collectIsDraggedAsState()
    val hours = remember {
        (0..23).toList()
    }
    val minutes = remember {
        (0..59).toList()
    }
    val seconds = remember {
        (0..59).toList()
    }
    LaunchedEffect(hourState, minuteState, secondState) {
        snapshotFlow {
            Triple(
                hours[hourState.firstVisibleItemIndex],
                minutes[minuteState.firstVisibleItemIndex],
                seconds[secondState.firstVisibleItemIndex],
            )
        }.filter {
            !isHourDragged && !isMinuteDragged && !isSecondDragged
        }.distinctUntilChanged()
            .collectLatest {
                onChange.invoke(it)
            }
    }
    Row(modifier = modifier) {
        Wheel(
            state = hourState,
            itemCount = hours.count(),
            itemHeight = itemHeight,
            visibleCount = visibleCount,
            flingBehavior = flingBehavior(hourState),
            draw = draw,
            animator = animator
        ) {
            content(IndexedValue(0, hours[it]))
        }
        Wheel(
            state = minuteState,
            itemCount = minutes.count(),
            itemHeight = itemHeight,
            visibleCount = visibleCount,
            flingBehavior = flingBehavior(minuteState),
            draw = draw,
            animator = animator
        ) {
            content(IndexedValue(1, minutes[it]))
        }
        Wheel(
            state = secondState,
            itemCount = seconds.count(),
            itemHeight = itemHeight,
            visibleCount = visibleCount,
            flingBehavior = flingBehavior(secondState),
            draw = draw,
            animator = animator
        ) {
            content(IndexedValue(2, seconds[it]))
        }
    }
}

