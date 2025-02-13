package io.datou.develop

import android.Manifest
import android.app.Notification
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

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
    buildAction: (NotificationCompat.Builder) -> Unit = {}
) = NotificationCompat.Builder(App, channelId)
    .setContentTitle(title)
    .setContentText(content)
    .apply(buildAction)
    .build()

fun cancelNotification(id: Int) {
    NotificationManagerCompat.from(App).cancel(id)
}

fun createNotificationChannel(
    channelId: String,
    channelName: String,
    importance: Int = NotificationManagerCompat.IMPORTANCE_DEFAULT,
    buildAction: (NotificationChannelCompat.Builder) -> Unit = {}
) {
    val manager = NotificationManagerCompat.from(App)
    val find = manager.notificationChannelsCompat.map { it.id }.find { it == channelId }
    if (find != null) {
        return
    }
    manager.createNotificationChannel(
        NotificationChannelCompat.Builder(channelId, importance)
            .setName(channelName)
            .apply(buildAction)
            .build()
    )
}

val NotificationChannels: List<NotificationChannelCompat>
    get() {
        val manager = NotificationManagerCompat.from(App)
        return manager.notificationChannelsCompat
    }

