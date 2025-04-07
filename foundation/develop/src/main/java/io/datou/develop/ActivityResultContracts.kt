package io.datou.develop

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import android.os.Environment
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationManagerCompat
import androidx.core.net.toUri

object CustomActivityResultContracts {

    class NotificationPermission : ActivityResultContract<String?, Boolean>() {
        companion object {
            private const val IS_REQUEST_NOTIFICATION_PERMISSION =
                "is_request_notification_permission"

            fun areNotificationsEnabled(channelId: String? = null): Boolean {
                val notificationManager = NotificationManagerCompat.from(AppContext)
                val b = notificationManager.areNotificationsEnabled()
                return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
                    && !channelId.isNullOrEmpty()
                ) {
                    val channel =
                        notificationManager.getNotificationChannel(channelId) ?: return false
                    channel.importance > NotificationManagerCompat.IMPORTANCE_NONE && b
                } else {
                    b
                }
            }
        }

        private var _channelId: String? = null

        override fun createIntent(context: Context, input: String?): Intent {
            _channelId = input
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && !loadFromShared(
                    IS_REQUEST_NOTIFICATION_PERMISSION to false
                )
            ) {
                saveToShared(IS_REQUEST_NOTIFICATION_PERMISSION to true)
                ActivityResultContracts.RequestPermission()
                    .createIntent(context, Manifest.permission.POST_NOTIFICATIONS)
            } else {
                createNotificationSettingsIntent(context, _channelId)
            }
        }

        private fun createNotificationSettingsIntent(context: Context, channelId: String?): Intent {
            return Intent().apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    if (channelId.isNullOrEmpty() || !areNotificationsEnabled()) {
                        action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
                        putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                    } else {
                        action = Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS
                        putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                        putExtra(Settings.EXTRA_CHANNEL_ID, channelId)
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
            return if (areNotificationsEnabled(input)) {
                SynchronousResult(true)
            } else {
                null
            }
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Boolean {
            return areNotificationsEnabled(_channelId)
        }
    }

    class UnknownAppInstallPermission : ActivityResultContract<Unit, Boolean>() {
        companion object {
            fun canRequestPackageInstalls(context: Context): Boolean {
                return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    context.packageManager.canRequestPackageInstalls()
                } else {
                    true
                }
            }
        }

        @RequiresApi(Build.VERSION_CODES.O)
        override fun createIntent(context: Context, input: Unit): Intent {
            return Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES).apply {
                data = "package:${context.packageName}".toUri()
            }
        }

        override fun getSynchronousResult(
            context: Context,
            input: Unit
        ): SynchronousResult<Boolean>? {
            return if (canRequestPackageInstalls(context)) {
                SynchronousResult(true)
            } else {
                null
            }
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Boolean {
            return canRequestPackageInstalls(AppContext)
        }
    }

    class OverlayDrawPermission : ActivityResultContract<Unit, Boolean>() {
        companion object {
            fun canDrawOverlays(context: Context): Boolean {
                return Settings.canDrawOverlays(context)
            }
        }

        override fun createIntent(context: Context, input: Unit): Intent {
            return Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
        }

        override fun getSynchronousResult(
            context: Context,
            input: Unit
        ): SynchronousResult<Boolean>? {
            return if (canDrawOverlays(context)) {
                SynchronousResult(true)
            } else {
                null
            }
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Boolean {
            return canDrawOverlays(AppContext)
        }
    }

    class ExternalStorageAccessPermission(
        private val useSAF: Boolean = true
    ) : ActivityResultContract<Unit, Boolean>() {
        companion object {
            private val EXTERNAL_STORAGE_PERMISSIONS = arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )

            fun isExternalStorageManager(useSAF: Boolean = true): Boolean {
                return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    if (useSAF) {
                        true
                    } else {
                        Environment.isExternalStorageManager()
                    }
                } else {
                    EXTERNAL_STORAGE_PERMISSIONS.all {
                        ContextCompat.checkSelfPermission(
                            AppContext,
                            it
                        ) == PackageManager.PERMISSION_GRANTED
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
                ActivityResultContracts.RequestMultiplePermissions()
                    .createIntent(context, EXTERNAL_STORAGE_PERMISSIONS)
            }
        }

        override fun getSynchronousResult(
            context: Context,
            input: Unit
        ): SynchronousResult<Boolean>? {
            return if (isExternalStorageManager(useSAF)) {
                SynchronousResult(true)
            } else {
                null
            }
        }

        override fun parseResult(
            resultCode: Int,
            intent: Intent?
        ): Boolean {
            return isExternalStorageManager()
        }
    }
}