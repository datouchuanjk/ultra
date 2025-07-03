package io.composex.ui.wheel

import android.util.Log
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.GraphicsLayerScope
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.unit.Dp
import io.composex.util.dayRangeInMonth
import io.composex.util.month
import io.composex.util.year
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import java.util.Calendar

@Composable
fun DatePicker(
    modifier: Modifier = Modifier,
    yearRange: IntRange = 1970..2025,
    state: @Composable (Int) -> LazyListState = { rememberLazyListState() },
    itemHeight: Dp = WheelDefaults.itemHeight,
    visibleCount: Int = WheelDefaults.VISIBLE_COUNT,
    flingBehavior: @Composable (LazyListState) -> FlingBehavior = { rememberSnapFlingBehavior(it) },
    draw: ContentDrawScope.(Rect) -> Unit = WheelDefaults.draw,
    animator: GraphicsLayerScope.(Float) -> Unit = WheelDefaults.animator,
    onChange: (Triple<Int, Int, Int>) -> Unit,
    content: @Composable (IndexedValue<Int>) -> Unit
) {
    val yearState = state(0)
    val monthState = state(1)
    val dayState = state(2)
    val isYearDragged by yearState.interactionSource.collectIsDraggedAsState()
    val isMonthDragged by monthState.interactionSource.collectIsDraggedAsState()
    val isDayDragged by dayState.interactionSource.collectIsDraggedAsState()
    val years = remember {
        yearRange.toList()
    }
    val months = remember {
        (0..11).toList()
    }
    val yearFirstVisibleItemIndex by remember {
        derivedStateOf {
            yearState.firstVisibleItemIndex
        }
    }
    val monthFirstVisibleItemIndex by remember {
        derivedStateOf {
            monthState.firstVisibleItemIndex
        }
    }
    val days by remember(
        yearFirstVisibleItemIndex,
        monthFirstVisibleItemIndex
    ) {
        derivedStateOf {
            val calendar = Calendar.getInstance()
            calendar.year = years[yearFirstVisibleItemIndex]
            calendar.month = months[monthFirstVisibleItemIndex]
            calendar.dayRangeInMonth.toList()
        }
    }
    LaunchedEffect(yearState, monthState, dayState) {
        snapshotFlow {
            Triple(
                years[yearState.firstVisibleItemIndex],
                months[monthState.firstVisibleItemIndex],
                days[dayState.firstVisibleItemIndex.coerceIn(0, days.lastIndex)]
            )
        }.filter {
            !isYearDragged && !isMonthDragged && !isDayDragged
        }.distinctUntilChanged()
            .collectLatest {
                onChange.invoke(it)
            }
    }
    Row(modifier = modifier) {
        Wheel(
            state = yearState,
            itemCount = years.count(),
            itemHeight = itemHeight,
            visibleCount = visibleCount,
            flingBehavior = flingBehavior(yearState),
            draw = draw,
            animator = animator
        ) {
            content(IndexedValue(0, years[it]))
        }
        Wheel(
            state = monthState,
            itemCount = months.count(),
            itemHeight = itemHeight,
            visibleCount = visibleCount,
            flingBehavior = flingBehavior(monthState),
            draw = draw,
            animator = animator
        ) {
            content(IndexedValue(1, months[it]))
        }
        Wheel(
            state = dayState,
            itemCount = days.count(),
            itemHeight = itemHeight,
            visibleCount = visibleCount,
            flingBehavior = flingBehavior(dayState),
            draw = draw,
            animator = animator
        ) {
            content(IndexedValue(2, days[it]))
        }
    }
}