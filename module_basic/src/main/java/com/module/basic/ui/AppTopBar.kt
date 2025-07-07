package com.module.basic.ui

import androidx.activity.OnBackPressedDispatcherOwner
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.composex.nav.NavBackHandler

/**
 * 通用头部
 */
@Composable
fun AppTopBar(
    title: String,
    showIcon: Boolean = true,
    onBack:(OnBackPressedDispatcherOwner?)-> Unit = {
        it?.onBackPressedDispatcher?.onBackPressed()
    }
) {
    val onBackPressedDispatcherOwner = LocalOnBackPressedDispatcherOwner.current
    NavBackHandler()
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .height(57.dp)
            .padding(horizontal = 15.dp)
    ) {
        if (showIcon) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = null,
                modifier = Modifier
                    .clip(CircleShape)
                    .align(alignment = Alignment.CenterStart)
                    .clickable {
                        onBack(onBackPressedDispatcherOwner)
                    })
        }
        Text(
            text = title, fontSize = 18.sp, color = Color.Black,
            modifier = Modifier.align(alignment = Alignment.Center)
        )
    }
}