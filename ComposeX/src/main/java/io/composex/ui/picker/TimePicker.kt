package io.composex.ui.picker

import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.GraphicsLayerScope
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.unit.Dp
import io.composex.ui.wheel.Wheel
import io.composex.ui.wheel.WheelDefaults
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged

@Composable
fun TimePicker(
    modifier: Modifier = Modifier,
    hourState: LazyListState = rememberLazyListState(),
    minuteState: LazyListState = rememberLazyListState(),
    secondState: LazyListState = rememberLazyListState(),
    itemHeight: Dp = WheelDefaults.itemHeight,
    visibleCount: Int = WheelDefaults.VISIBLE_COUNT,
    flingBehavior: @Composable (LazyListState) -> FlingBehavior = { rememberSnapFlingBehavior(it) },
    draw: ContentDrawScope.(Rect) -> Unit = WheelDefaults.draw,
    animator: GraphicsLayerScope.(Float) -> Unit = WheelDefaults.animator,
    onChange: (Triple<Int, Int, Int>) -> Unit,
    content: @Composable (IndexedValue<Int>) -> Unit,
) {
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
                hours[hourState.firstVisibleItemIndex.coerceIn(0, hours.lastIndex)],
                minutes[minuteState.firstVisibleItemIndex.coerceIn(0, hours.lastIndex)],
                seconds[secondState.firstVisibleItemIndex.coerceIn(0, hours.lastIndex)],
            )
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
            content(IndexedValue(0, hours[it.coerceIn(0, hours.lastIndex)]))
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
            content(IndexedValue(1, minutes[it.coerceIn(0, minutes.lastIndex)]))
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
            content(IndexedValue(2, seconds[it.coerceIn(0, seconds.lastIndex)]))
        }
    }
}

