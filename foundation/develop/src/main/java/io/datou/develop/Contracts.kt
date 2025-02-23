package io.datou.develop

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContract
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat

object CustomActivityResultContracts {
    class OpenNotificationSettings : ActivityResultContract<String?, Boolean>() {
        companion object {
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

        override fun parseResult(resultCode: Int, intent: Intent?): Boolean {
            return _channelId?.let {
                areNotificationChannelEnabled(it)
            } ?: areNotificationsEnabled()
        }
    }

    class RequestPackageInstalls : ActivityResultContract<Unit, Boolean>() {
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

        override fun parseResult(resultCode: Int, intent: Intent?): Boolean {
            return canRequestPackageInstalls()
        }
    }

    class OpenPermissionSettings : ActivityResultContract<Array<String>, Boolean>() {
        companion object {
            internal fun isPermissionGranted(vararg name: String): Boolean {
                return name.all {
                    ContextCompat.checkSelfPermission(App, it) == PERMISSION_GRANTED
                }
            }

            fun isPermissionPermanentlyDenied(vararg name: String): Boolean {
                return name.all {
                    !isPermissionGranted(it) && StackTopActivity?.let { activity ->
                        !ActivityCompat.shouldShowRequestPermissionRationale(activity, it)
                    } ?: false
                }
            }
        }

        private var _permissions: Array<String> = arrayOf()
        override fun createIntent(context: Context, input: Array<String>): Intent {
            _permissions = input
            return Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.fromParts("package", context.packageName, null)
            }
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Boolean {
            return isPermissionGranted(*_permissions)
        }
    }
}