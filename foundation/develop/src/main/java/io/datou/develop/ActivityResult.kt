package io.datou.develop

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityOptionsCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import androidx.core.net.toUri

fun ActivityResultLauncher<PickVisualMediaRequest>.launchImageOnly(
    options: ActivityOptionsCompat? = null
) {
    launch(
        PickVisualMediaRequest(PickVisualMedia.ImageOnly),
        options
    )
}

fun ActivityResultLauncher<PickVisualMediaRequest>.launchVideoOnly(
    options: ActivityOptionsCompat? = null,
) {
    launch(
        PickVisualMediaRequest(PickVisualMedia.VideoOnly),
        options
    )
}

fun ActivityResultLauncher<PickVisualMediaRequest>.launchImageAndVideo(
    options: ActivityOptionsCompat? = null,
) {
    launch(
        PickVisualMediaRequest(PickVisualMedia.ImageAndVideo),
        options
    )
}

object CustomActivityResultContracts {

    class NotificationPermission : ActivityResultContract<String?, Boolean>() {
        companion object {
            private const val IS_REQUEST_NOTIFICATION_PERMISSION =
                "is_request_notification_permission"

            fun areNotificationsEnabled(channelId: String? = null): Boolean {
                val notificationManager = NotificationManagerCompat.from(AppContext)
                return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && !channelId.isNullOrEmpty()) {
                    notificationManager.getNotificationChannel(channelId)?.let {
                        it.importance > NotificationManagerCompat.IMPORTANCE_NONE
                    } ?: false
                } else {
                    true
                } && notificationManager.areNotificationsEnabled()
            }
        }

        private var _channelId: String? = null
        private val _sp = AppContext.getSharedPreferences(
            "${AppContext.packageName}_NotificationPermission",
            Context.MODE_PRIVATE
        )

        override fun createIntent(context: Context, input: String?): Intent {
            _channelId = input
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
                && !_sp.getBoolean(IS_REQUEST_NOTIFICATION_PERMISSION, false)
            ) {
                _sp.edit {
                    putBoolean(IS_REQUEST_NOTIFICATION_PERMISSION, true)
                }
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
            fun canDrawOverlays(): Boolean {
                return Settings.canDrawOverlays(AppContext)
            }
        }

        override fun createIntent(context: Context, input: Unit): Intent {
            return Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
        }

        override fun getSynchronousResult(
            context: Context,
            input: Unit
        ): SynchronousResult<Boolean>? {
            return if (canDrawOverlays()) {
                SynchronousResult(true)
            } else {
                null
            }
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Boolean {
            return canDrawOverlays()
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


