package io.standard.composable.picker

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.flow.collectLatest
import java.util.Calendar

@Composable
fun TimePicker(
    modifier: Modifier = Modifier,
    textStyle: TextStyle = TextStyle(
        color = Color.Black,
        fontSize = 14.sp
    ),
    block: (Calendar) -> Unit
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
    val hourState = rememberLazyListState()
    val hourIndex by remember { derivedStateOf { hourState.firstVisibleItemIndex } }
    val minuteState = rememberLazyListState()
    val minuteIndex by remember { derivedStateOf { minuteState.firstVisibleItemIndex } }
    val secondState = rememberLazyListState()
    val secondIndex by remember { derivedStateOf { secondState.firstVisibleItemIndex } }

    LaunchedEffect(hourIndex, minuteIndex, secondIndex) {
        snapshotFlow {
            Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, hours[hourIndex])
                set(Calendar.MINUTE, minutes[minuteIndex])
                set(Calendar.SECOND, seconds[secondIndex])
            }
        }.collectLatest {
            block(it)
        }
    }
    Row(
        modifier = modifier,
    ) {
        Wheel(
            itemCount = hours.count(),
            state = hourState
        ) {
            Text(text = "${hours[it]}时", style = textStyle)
        }
        Wheel(
            itemCount = minutes.count(),
            state = minuteState
        ) {
            Text(text = "${minutes[it]}分", style = textStyle)
        }
        Wheel(
            itemCount = seconds.count(),
            state = secondState
        ) {
            Text(text = "${seconds[it]}秒", style = textStyle)
        }
    }
}