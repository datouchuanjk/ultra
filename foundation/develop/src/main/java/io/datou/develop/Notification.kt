package io.datou.develop

import android.Manifest
import android.app.Notification
import android.media.AudioAttributes
import android.media.RingtoneManager
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.graphics.drawable.IconCompat

@RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
fun Notification.show(
    id: Int = System.currentTimeMillis().toInt(),
) {
    NotificationManagerCompat.from(App).notify(id, this)
}

fun createNotification(
    channelId: String,
    title: String,
    content: String,
    smallIcon: Int,
    autoCancel: Boolean = true,
    useDefaultSound: Boolean = true,
    buildAction: (NotificationCompat.Builder) -> Unit = {}
) = NotificationCompat.Builder(App, channelId)
    .setContentTitle(title)
    .setContentText(content)
    .setSmallIcon(smallIcon)
    .apply {
        setAutoCancel(autoCancel)
        if (useDefaultSound) {
            setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
        }
    }
    .apply(buildAction)
    .build()

fun cancelNotification(id: Int) {
    NotificationManagerCompat.from(App).cancel(id)
}

fun createNotificationChannel(
    channelId: String,
    channelName: String,
    importance: Int = NotificationManagerCompat.IMPORTANCE_DEFAULT,
    useDefaultSound: Boolean = true,
    buildAction: (NotificationChannelCompat.Builder) -> Unit = {}
) {
    val manager = NotificationManagerCompat.from(App)
    val find = manager.notificationChannelsCompat.map { it.id }.find { it == channelId }
    if (find != null) {
        return
    }
    var newImportance = importance
    if (useDefaultSound) {
        if (importance <= NotificationManagerCompat.IMPORTANCE_DEFAULT) {
            newImportance = NotificationManagerCompat.IMPORTANCE_HIGH
        }
    }
    manager.createNotificationChannel(
        NotificationChannelCompat.Builder(channelId, newImportance)
            .setName(channelName)
            .apply {
                if (useDefaultSound) {
                    setSound(
                        RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION),
                        AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                            .build()
                    )
                }
            }
            .apply(buildAction)
            .build()
    )
}

