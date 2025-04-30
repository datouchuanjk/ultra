package io.datou.picker

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.flow.collectLatest
import java.util.Calendar

@Composable
fun DatePicker(
    modifier: Modifier = Modifier,
    yearRange: List<Int> = remember {
        (1970..Calendar.getInstance().get(Calendar.YEAR)).toList()
    },
    textStyle: TextStyle = TextStyle(
        color = Color.Black,
        fontSize = 14.sp
    ),
    block: (Calendar) -> Unit
) {
    val monthRange = remember {
        mutableStateListOf<Int>()
    }
    val dayRange = remember {
        mutableStateListOf<Int>()
    }
    val yearState = rememberLazyListState()
    val yearIndex by remember { derivedStateOf { yearState.firstVisibleItemIndex } }
    val monthState = rememberLazyListState()
    val monthIndex by remember { derivedStateOf { monthState.firstVisibleItemIndex } }
    val dayState = rememberLazyListState()
    val dayIndex by remember { derivedStateOf { dayState.firstVisibleItemIndex } }
    val calendar = remember {
        Calendar.getInstance()
    }
    LaunchedEffect(yearIndex, monthIndex,dayIndex) {
        calendar.set(Calendar.YEAR, yearRange[yearIndex])
        calendar.set(Calendar.MONTH, monthRange[monthIndex])
        calendar.set(Calendar.DAY_OF_MONTH, dayRange[dayIndex])
        monthRange.clear()
        monthRange.addAll(
            calendar.getActualMinimum(Calendar.MONTH)..
                    calendar.getActualMaximum(Calendar.MONTH)
        )
        dayRange.clear()
        dayRange.addAll(
            calendar.getActualMinimum(Calendar.DAY_OF_MONTH)..
                    calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        )
        snapshotFlow{calendar}.collectLatest {
            block(it)
        }
    }
    Row(
        modifier = modifier,
    ) {
        Wheel(
            itemCount = yearRange.count(),
            state = yearState
        ) {
            Text(text = "${yearRange[it]}年", style = textStyle)
        }
        Wheel(
            itemCount = monthRange.count(),
            state = monthState
        ) {
            Text(text = "${monthRange[it] + 1}月", style = textStyle)
        }
        Wheel(
            itemCount = dayRange.count(),
            state = dayState
        ) {
            Text(text = "${dayRange[it]}日", style = textStyle)
        }
    }
}