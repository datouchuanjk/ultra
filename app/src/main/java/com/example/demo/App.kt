package com.example.demo

import android.app.Application
import com.module.basic.constant.NotificationConstant
import dagger.hilt.android.HiltAndroidApp
import io.composex.util.createNotificationChannel

@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel(
            channelId = NotificationConstant.CHANNEL_ID,
            channelName =  NotificationConstant.CHANNEL_ID
        )
    }
}