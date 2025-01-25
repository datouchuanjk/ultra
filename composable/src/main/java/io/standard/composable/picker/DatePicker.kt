package io.standard.composable.picker

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
    fromYear: Int = 1970,
    toYear: Int = Calendar.getInstance().get(Calendar.YEAR),
    textStyle: TextStyle = TextStyle(
        color = Color.Black,
        fontSize = 14.sp
    ),
    block: (Calendar) -> Unit
) {
    val years = remember {
        (fromYear..toYear).toList()
    }
    val months = remember {
        mutableStateListOf<Int>()
    }
    val days = remember {
        mutableStateListOf<Int>()
    }
    val yearState = rememberLazyListState()
    val yearIndex by remember { derivedStateOf { yearState.firstVisibleItemIndex } }
    val monthState = rememberLazyListState()
    val monthIndex by remember { derivedStateOf { monthState.firstVisibleItemIndex } }
    val dayState = rememberLazyListState()
    val dayIndex by remember { derivedStateOf { dayState.firstVisibleItemIndex } }

    LaunchedEffect(yearIndex) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, years[yearIndex])
        months.clear()
        months.addAll(
            calendar.getActualMinimum(Calendar.MONTH)..
                    calendar.getActualMaximum(Calendar.MONTH)
        )
    }
    LaunchedEffect(yearIndex, monthIndex) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, years[yearIndex])
        calendar.set(Calendar.MONTH, months[monthIndex])
        days.clear()
        days.addAll(
            calendar.getActualMinimum(Calendar.DAY_OF_MONTH)..
                    calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        )
    }
    LaunchedEffect(yearIndex, monthIndex, dayIndex) {
        snapshotFlow {
            Calendar.getInstance().apply {
                set(Calendar.YEAR, years[yearIndex])
                set(Calendar.MONTH, months[monthIndex])
                set(Calendar.DAY_OF_MONTH, days[dayIndex])
            }
        }.collectLatest {
            block(it)
        }
    }
    Row(
        modifier = modifier,
    ) {
        Wheel(
            itemCount = years.count(),
            state = yearState
        ) {
            Text(text = "${years[it]}年", style = textStyle)
        }
        Wheel(
            itemCount = months.count(),
            state = monthState
        ) {
            Text(text = "${months[it] + 1}月", style = textStyle)
        }
        Wheel(
            itemCount = days.count(),
            state = dayState
        ) {
            Text(text = "${days[it]}日", style = textStyle)
        }
    }
}