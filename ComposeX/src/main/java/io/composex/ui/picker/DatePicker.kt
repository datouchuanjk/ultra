package io.composex.ui.picker

import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.GraphicsLayerScope
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.unit.Dp
import io.composex.ui.wheel.Wheel
import io.composex.ui.wheel.WheelDefaults
import io.composex.util.dayOfMonth
import io.composex.util.dayOfYear
import io.composex.util.dayRangeInMonth
import io.composex.util.isSomeMonth
import io.composex.util.isSomeYear
import io.composex.util.maxDayInMonth
import io.composex.util.maxMonth
import io.composex.util.minDayInMonth
import io.composex.util.minDayInYear
import io.composex.util.minMonth
import io.composex.util.month
import io.composex.util.monthRange
import io.composex.util.year
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import java.util.Calendar

@Composable
fun DatePicker(
    modifier: Modifier = Modifier,
    beginDate: Calendar = rememberSaveable {
        Calendar.getInstance().apply {
            year = 1970
            dayOfYear = minDayInYear
        }
    },
    endDate: Calendar = rememberSaveable {
        Calendar.getInstance()
    },
    selectedDate: Calendar = rememberSaveable {
        beginDate
    },
    yearState: LazyListState = rememberLazyListState(
        initialFirstVisibleItemIndex = (beginDate.year..endDate.year).indexOf(selectedDate.year),
    ),
    monthState: LazyListState = rememberLazyListState(
        initialFirstVisibleItemIndex = selectedDate.monthRange.indexOf(selectedDate.month),
    ),
    dayState: LazyListState = rememberLazyListState(
        initialFirstVisibleItemIndex = selectedDate.dayRangeInMonth.indexOf(selectedDate.dayOfMonth),
    ),
    itemHeight: Dp = WheelDefaults.itemHeight,
    visibleCount: Int = WheelDefaults.VISIBLE_COUNT,
    flingBehavior: @Composable (LazyListState) -> FlingBehavior = { rememberSnapFlingBehavior(it) },
    draw: ContentDrawScope.(Rect) -> Unit = WheelDefaults.draw,
    animator: GraphicsLayerScope.(Float) -> Unit = WheelDefaults.animator,
    onChange: (Triple<Int, Int, Int>) -> Unit,
    content: @Composable (IndexedValue<Int>) -> Unit
) {
    val yearFirstVisibleItemIndex by remember(yearState) {
        derivedStateOf {
            yearState.firstVisibleItemIndex
        }
    }
    val monthFirstVisibleItemIndex by remember(monthState) {
        derivedStateOf {
            monthState.firstVisibleItemIndex
        }
    }
    val years = remember {
        (beginDate.year..endDate.year).toList()
    }
    val months by remember(yearFirstVisibleItemIndex, years) {
        derivedStateOf {
            val calendar = Calendar.getInstance()
            calendar.year = years[yearFirstVisibleItemIndex.coerceIn(0, years.lastIndex)]
            when {
                calendar.isSomeYear(beginDate) -> beginDate.month..beginDate.maxMonth
                calendar.isSomeYear(endDate) -> endDate.minMonth..endDate.month
                else -> calendar.monthRange
            }.toList()
        }
    }
    val days by remember(
        yearFirstVisibleItemIndex,
        monthFirstVisibleItemIndex,
        years,
        months
    ) {
        derivedStateOf {
            val calendar = Calendar.getInstance()
            calendar.year = years[yearFirstVisibleItemIndex.coerceIn(0, years.lastIndex)]
            calendar.month = months[monthFirstVisibleItemIndex.coerceIn(0, months.lastIndex)]
            when {
                calendar.isSomeMonth(beginDate) -> beginDate.dayOfMonth..beginDate.maxDayInMonth
                calendar.isSomeMonth(endDate) -> endDate.minDayInMonth..endDate.dayOfMonth
                else -> calendar.dayRangeInMonth
            }.toList()
        }
    }
    LaunchedEffect(yearState, monthState, dayState, days) {
        snapshotFlow {
            Triple(
                years[yearState.firstVisibleItemIndex.coerceIn(0, years.lastIndex)],
                months[monthState.firstVisibleItemIndex.coerceIn(0, months.lastIndex)],
                days[dayState.firstVisibleItemIndex.coerceIn(0, days.lastIndex)]
            )
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
            content(IndexedValue(0, years[it.coerceIn(0, years.lastIndex)]))
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
            content(IndexedValue(1, months[it.coerceIn(0, months.lastIndex)]))
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
            content(IndexedValue(2, days[it.coerceIn(0, days.lastIndex)]))
        }
    }
}