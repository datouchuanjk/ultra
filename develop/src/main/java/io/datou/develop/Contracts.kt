package io.datou.develop

import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContract

object CustomActivityResultContracts {

    class OpenNotificationSettings : ActivityResultContract<String?, Boolean>() {
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
            return if (_channelId.isNullOrEmpty()) {
                areNotificationsEnabled()
            } else {
                areNotificationChannelEnabled(_channelId!!)
            }
        }
    }
}