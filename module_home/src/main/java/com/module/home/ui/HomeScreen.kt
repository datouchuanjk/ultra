package com.module.home.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.module.home.viewmodel.HomeViewModel
import io.composex.ui.wheel.DatePicker
import io.composex.util.dayOfMonth
import io.composex.util.dayRangeInMonth
import io.composex.util.month
import io.composex.util.year
import java.util.Calendar

@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel()) {
    Column(modifier = Modifier.fillMaxSize()) {
        val yearRange = (2020..2025)
        val a = Calendar.getInstance()
        DatePicker(
            yearRange = yearRange,
            state = {
                when (it) {
                    0 -> rememberLazyListState()
                    1 -> rememberLazyListState()
                    2 -> rememberLazyListState()
                    else -> throw NullPointerException()
                }
            },
            onChange = {

            }) {
            val v = if (it.index == 1) {
                it.value + 1
            } else {
                it.value
            }
            Text(
                text = v.toString().padStart(2, '0'),
                fontSize = 12.sp,
                color = Color.Black
            )
        }
    }
}