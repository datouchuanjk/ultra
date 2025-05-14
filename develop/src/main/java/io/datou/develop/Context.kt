package io.datou.develop

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Process
import android.util.DisplayMetrics
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.getSystemService


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

val Context.isMainProcess: Boolean
    get() {
        return processName == packageName
    }

val Context.processName: String?
    get() = getSystemService<ActivityManager>()
        ?.runningAppProcesses
        ?.find { it.pid == Process.myPid() }
        ?.processName

val Context.versionCode: Long
    get() =
        AppContext.packageManager.getPackageInfo(AppContext.packageName, 0).run {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                longVersionCode
            } else {
                versionCode.toLong()
            }
        }

val Context.versionName: String
    get() = AppContext.packageManager.getPackageInfo(
        AppContext.packageName,
        0
    ).versionName.orEmpty()

val Context.installedPackages: List<PackageInfo>
    get() = AppContext.packageManager.getInstalledPackages(0)

val Context.metaData: Bundle
    get() = AppContext.packageManager.getApplicationInfo(
        AppContext.packageName,
        PackageManager.GET_META_DATA
    ).metaData

fun Context.createConfigurationContext(adapterWidth: Float): Context {
    val newDensityDpi = resources.displayMetrics.widthPixels
        .div(adapterWidth)
        .times(DisplayMetrics.DENSITY_DEFAULT)
    val configuration = resources.configuration
    configuration.fontScale = 1f
    configuration.densityDpi = newDensityDpi.toInt()
    return createConfigurationContext(configuration)
}


