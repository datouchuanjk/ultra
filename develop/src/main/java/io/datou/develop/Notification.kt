package io.datou.develop

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationManager
import android.os.Build
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

@SuppressLint("MissingPermission")
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

fun areNotificationsEnabled(): Boolean {
    val manager = NotificationManagerCompat.from(App)
    return manager.areNotificationsEnabled()
}

fun areNotificationChannelEnabled(channelId: String): Boolean {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val manager = NotificationManagerCompat.from(App)
        val channel = manager.getNotificationChannel(channelId) ?: return false
        return channel.importance > NotificationManager.IMPORTANCE_NONE
                && areNotificationsEnabled()
    } else {
        return areNotificationsEnabled()
    }
}

val NotificationChannels: List<NotificationChannelCompat>
    get() {
        val manager = NotificationManagerCompat.from(App)
        return manager.notificationChannelsCompat
    }

