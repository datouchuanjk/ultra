package io.composex.util

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

@RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
fun Context.notify(
    id: Int = System.currentTimeMillis().toInt(),
    channelId: String,
    title: String,
    content: String,
    smallIcon: Int = applicationInfo.icon,
    autoCancel: Boolean = true,
    activityIntent: Intent? = null,
    buildAction: NotificationCompat.Builder.() -> Unit = {}
) {
    NotificationCompat.Builder(this, channelId)
        .setContentTitle(title)
        .setContentText(content)
        .setSmallIcon(smallIcon)
        .setAutoCancel(autoCancel)
        .setContentActivityIntent(this, activityIntent)
        .apply(buildAction)
        .build()
        .let {
            NotificationManagerCompat.from(this).notify(id, it)
        }

}

fun Context.cancelNotification(id: Int) {
    NotificationManagerCompat.from(this).cancel(id)
}

fun Context.createNotificationChannel(
    channelId: String,
    importance: Int = NotificationManagerCompat.IMPORTANCE_DEFAULT,
    channelName: String = channelId,
    buildAction: NotificationChannelCompat.Builder.() -> Unit = {}
) {
    val manager = NotificationManagerCompat.from(this)
    if (manager.getNotificationChannel(channelId) == null) {
        manager.createNotificationChannel(
            NotificationChannelCompat.Builder(channelId, importance)
                .setName(channelName)
                .apply(buildAction)
                .build()
        )
    }
}

fun Context.deleteNotificationChannel(channelId: String) {
    val manager = NotificationManagerCompat.from(this)
    if (manager.getNotificationChannel(channelId) != null) {
        manager.deleteNotificationChannel(channelId)
    }
}

private fun NotificationCompat.Builder.setContentActivityIntent(context: Context, intent: Intent?) =
    apply {
        intent?.let {
            setContentIntent(
                PendingIntent.getActivity(
                    context,
                    0,
                    it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK),
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
            )
        }
    }