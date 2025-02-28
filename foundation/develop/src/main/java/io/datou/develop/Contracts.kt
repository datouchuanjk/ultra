package io.datou.develop

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContract
import androidx.core.app.NotificationManagerCompat

object CustomActivityResultContracts {
    class AppNotificationSettings : ActivityResultContract<String?, Boolean>() {
        companion object {
            fun areNotificationsEnabled(channelId: String? = null): Boolean {
                val manager = NotificationManagerCompat.from(App)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && channelId != null) {
                    val channel = manager.getNotificationChannel(channelId) ?: return false
                    return channel.importance > NotificationManager.IMPORTANCE_NONE
                            && manager.areNotificationsEnabled()
                } else {
                    return manager.areNotificationsEnabled()
                }
            }
        }
        private var _channelId: String? = null
        override fun createIntent(context: Context, input: String?): Intent {
            _channelId = input
            return Intent().apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    if (_channelId.isNullOrEmpty()) {
                        action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
                        putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                    } else {
                        action = Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS
                        putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                        putExtra(Settings.EXTRA_CHANNEL_ID, _channelId)
                    }
                } else {
                    action = "android.settings.APP_NOTIFICATION_SETTINGS"
                    putExtra("app_package", context.packageName)
                    putExtra("app_uid", context.applicationInfo.uid)
                }
            }
        }

        override fun getSynchronousResult(
            context: Context,
            input: String?
        ): SynchronousResult<Boolean>? {
            if (areNotificationsEnabled(input)) {
                return SynchronousResult(true)
            }
            return null
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Boolean {
            return areNotificationsEnabled(_channelId)
        }
    }

    class ManageUnknownAppSource : ActivityResultContract<Unit, Boolean>() {
        companion object {
            fun canRequestPackageInstalls(): Boolean {
                return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    App.packageManager.canRequestPackageInstalls()
                } else {
                    true
                }
            }
        }

        override fun createIntent(context: Context, input: Unit): Intent {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES).apply {
                    data = Uri.parse("package:${context.packageName}")
                }
            } else {
                Intent(Intent.ACTION_VIEW)
            }
        }

        override fun getSynchronousResult(
            context: Context,
            input: Unit
        ): SynchronousResult<Boolean>? {
            if (canRequestPackageInstalls()) {
                return SynchronousResult(true)
            }
            return null
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Boolean {
            return canRequestPackageInstalls()
        }
    }

    class ManageOverlayPermissions : ActivityResultContract<Unit, Boolean>() {
        companion object {
            fun canDrawOverlays(): Boolean {
                return Settings.canDrawOverlays(App)
            }
        }

        override fun createIntent(context: Context, input: Unit): Intent {
            return Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
        }

        override fun getSynchronousResult(
            context: Context,
            input: Unit
        ): SynchronousResult<Boolean>? {
            if (canDrawOverlays()) {
                return SynchronousResult(true)
            }
            return null
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Boolean {
            return canDrawOverlays()
        }
    }

    class ApplicationDetailsSettings : ActivityResultContract<() -> Boolean, Boolean>() {
        private var _input: (() -> Boolean)? = null
        override fun createIntent(context: Context, input: () -> Boolean): Intent {
            _input = input
            return Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.parse("package:" + context.packageName)
            )
        }

        override fun getSynchronousResult(
            context: Context,
            input: () -> Boolean
        ): SynchronousResult<Boolean>? {
            if (input()) {
                return SynchronousResult(true)
            }
            return null
        }

        override fun parseResult(resultCode: Int, intent: Intent?):Boolean {
            return _input?.invoke() ?: false
        }
    }
}