package io.datou.develop

import android.Manifest
import android.app.Notification
import android.content.Context
import android.media.AudioAttributes
import android.media.RingtoneManager
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

internal val Context.notificationManagerCompat get() = NotificationManagerCompat.from(this)

@RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
fun Notification.show(
    id: Int = System.currentTimeMillis().toInt(),
): Int {
    AppContext.notificationManagerCompat.notify(id, this)
    return id
}

fun cancelNotification(id: Int) {
    AppContext.notificationManagerCompat.cancel(id)
}

fun createNotification(
    channelId: String,
    title: String,
    content: String,
    smallIcon: Int,
    autoCancel: Boolean = true,
    useDefaultSound: Boolean = true,
    buildAction: (NotificationCompat.Builder) -> Unit = {}
) = NotificationCompat.Builder(AppContext, channelId)
    .setContentTitle(title)
    .setContentText(content)
    .setSmallIcon(smallIcon)
    .setAutoCancel(autoCancel)
    .apply {
        if (useDefaultSound) {
            setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
        }
    }
    .apply(buildAction)
    .build()


fun createNotificationChannel(
    channelId: String,
    channelName: String = channelId,
    importance: Int = NotificationManagerCompat.IMPORTANCE_DEFAULT,
    useDefaultSound: Boolean = true,
    buildAction: (NotificationChannelCompat.Builder) -> Unit = {}
) {
    if (
        AppContext
            .notificationManagerCompat
            .notificationChannelsCompat
            .find {
                it.id == channelId
            } != null
    ) {
        return
    }
    var newImportance = importance
    if (useDefaultSound&&importance<=NotificationManagerCompat.IMPORTANCE_DEFAULT) {
            newImportance = NotificationManagerCompat.IMPORTANCE_HIGH
    }
    AppContext.notificationManagerCompat.createNotificationChannel(
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

