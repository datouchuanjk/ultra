package com.module.home.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.module.basic.constant.NotificationConstant
import com.module.home.viewmodel.HomeViewModel
import io.composex.util.CustomActivityResultContracts
import com.module.basic.util.HostIntent
import io.composex.util.notifyNotification

@SuppressLint("MissingPermission")
@Composable
fun HomeScreen(viewModel: HomeViewModel = viewModel()) {
    val context: Context = LocalContext.current
    Column(modifier = Modifier.fillMaxSize()) {
        val launcher = rememberLauncherForActivityResult(
            CustomActivityResultContracts.RequestNotificationPermission()
        ) {
            if (it) {
                context .notifyNotification(
                    channelId = NotificationConstant.CHANNEL_ID,
                    title = "标题",
                    content = "内容",
                    activityIntent = HostIntent
                )
            }
        }
        Button(onClick = {
            launcher.launch(NotificationConstant.CHANNEL_ID)
        }) {
            Text("打开一个通知栏呗")
        }
    }
}