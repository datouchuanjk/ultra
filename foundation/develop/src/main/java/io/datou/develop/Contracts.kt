package io.datou.develop

import android.Manifest
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri

object CustomActivityResultContracts {
    class AppNotificationSettings : ActivityResultContract<String?, Boolean>() {
        companion object {
            fun areNotificationsEnabled(channelId: String? = null): Boolean {
                val manager = NotificationManagerCompat.from(InstanceApp)
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
                    if (_channelId.isNullOrEmpty() || areNotificationsEnabled() == false) {
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
                    InstanceApp.packageManager.canRequestPackageInstalls()
                } else {
                    true
                }
            }
        }

        override fun createIntent(context: Context, input: Unit): Intent {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES).apply {
                    data = "package:${context.packageName}".toUri()
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
                return Settings.canDrawOverlays(InstanceApp)
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

    class ManageExternalStoragePermission : ActivityResultContract<Unit, Boolean>() {
        companion object {
            fun isExternalStorageManager(): Boolean {
                return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    Environment.isExternalStorageManager()
                } else {
                    arrayOf(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                    ).all {
                        ContextCompat.checkSelfPermission(InstanceApp, it) == PERMISSION_GRANTED
                    }
                }
            }
        }

        override fun createIntent(
            context: Context,
            input: Unit
        ): Intent {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION).apply {
                    data = Uri.fromParts("package", context.packageName, null)
                }
            } else {
                ActivityResultContracts.RequestMultiplePermissions().createIntent(
                    context,
                    arrayOf(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                    )
                )
            }
        }

        override fun getSynchronousResult(
            context: Context,
            input: Unit
        ): SynchronousResult<Boolean>? {
            if (isExternalStorageManager()) {
                return SynchronousResult(true)
            }
            return null
        }

        override fun parseResult(
            resultCode: Int,
            intent: Intent?
        ): Boolean {
            return isExternalStorageManager()
        }
    }
}
