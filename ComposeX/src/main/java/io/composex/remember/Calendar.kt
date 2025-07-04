package io.composex.remember

import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import java.util.Calendar

@Composable
fun rememberCalendar(
    vararg inputs: Any?,
    key: String? = null,
    init: () -> Calendar
) = rememberSaveable(
    inputs = inputs,
    saver = listSaver<Calendar, Long>(
        save = { listOf(it.timeInMillis) },
        restore = { Calendar.getInstance().apply { timeInMillis = it[0] } }
    ),
    key = key,
    init = init
)