package io.datou.develop

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import java.io.File
import java.io.FileNotFoundException

fun Context.findActivity(): Activity? {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) {
            return context
        }
        context = context.baseContext
    }
    return null
}

val VersionCode: Long
        by lazy {
            val packageInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                App.packageManager.getPackageInfo(
                    App.packageName,
                    PackageManager.PackageInfoFlags.of(0)
                )
            } else {
                App.packageManager.getPackageInfo(App.packageName, 0)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                packageInfo.longVersionCode
            } else {
                @Suppress("DEPRECATION")
                packageInfo.versionCode.toLong()
            }
        }

val VersionName: String by lazy {
    val packageInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        App.packageManager.getPackageInfo(
            App.packageName,
            PackageManager.PackageInfoFlags.of(0)
        )
    } else {
        App.packageManager.getPackageInfo(App.packageName, 0)
    }
    packageInfo.versionName.orEmpty()
}

val InstalledPackages: List<PackageInfo>
    get() {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            App.packageManager.getInstalledPackages(PackageManager.PackageInfoFlags.of(0))
        } else {
            App.packageManager.getInstalledPackages(0)
        }
    }

val MetaData: Bundle by lazy {
    val applicationInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        App.packageManager.getApplicationInfo(
            App.packageName,
            PackageManager.ApplicationInfoFlags.of(PackageManager.GET_META_DATA.toLong())
        )
    } else {
        App.packageManager.getApplicationInfo(App.packageName, PackageManager.GET_META_DATA)
    }
    applicationInfo.metaData
}

fun Context.createDensityContext(designWidth: Float): Context {
    val newDensityDpi = resources.displayMetrics.widthPixels
        .div(designWidth)
        .times(DisplayMetrics.DENSITY_DEFAULT)
    val configuration = resources.configuration
    configuration.fontScale = 1f
    configuration.densityDpi = newDensityDpi.toInt()
    return createConfigurationContext(configuration)
}
