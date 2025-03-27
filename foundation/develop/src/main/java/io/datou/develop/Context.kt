package io.datou.develop

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
fun findActivity() = LocalContext.current.findActivity()

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

val Context.versionCode: Long
    get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Instance.packageManager.getPackageInfo(
            Instance.packageName,
            PackageManager.PackageInfoFlags.of(0)
        )
    } else {
        Instance.packageManager.getPackageInfo(Instance.packageName, 0)
    }.run {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            longVersionCode
        } else {
            versionCode.toLong()
        }
    }

val Context.versionName: String
    get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Instance.packageManager.getPackageInfo(
            Instance.packageName,
            PackageManager.PackageInfoFlags.of(0)
        )
    } else {
        Instance.packageManager.getPackageInfo(Instance.packageName, 0)
    }.versionName.orEmpty()

val Context.installedPackages: List<PackageInfo>
    get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Instance.packageManager.getInstalledPackages(PackageManager.PackageInfoFlags.of(0))
    } else {
        Instance.packageManager.getInstalledPackages(0)
    }

val Context.metaData: Bundle
    get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Instance.packageManager.getApplicationInfo(
            Instance.packageName,
            PackageManager.ApplicationInfoFlags.of(PackageManager.GET_META_DATA.toLong())
        )
    } else {
        Instance.packageManager.getApplicationInfo(
            Instance.packageName,
            PackageManager.GET_META_DATA
        )
    }.metaData

fun Context.createDesignAdapterContext(designWidth: Float): Context {
    val newDensityDpi = resources.displayMetrics.widthPixels
        .div(designWidth)
        .times(DisplayMetrics.DENSITY_DEFAULT)
    val configuration = resources.configuration
    configuration.fontScale = 1f
    configuration.densityDpi = newDensityDpi.toInt()
    return createConfigurationContext(configuration)
}

