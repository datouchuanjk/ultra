package io.datou.develop

import android.content.pm.PackageManager.PERMISSION_GRANTED
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

fun isPermissionGranted(vararg name: String): Boolean {
    return name.all {
        ContextCompat.checkSelfPermission(App, it) == PERMISSION_GRANTED
    }
}

fun isPermissionPermanentlyDenied(vararg name: String): Boolean {
    return name.all {
        !isPermissionGranted(it) && PeekActivity?.let { activity ->
            !ActivityCompat.shouldShowRequestPermissionRationale(activity, it)
        } ?: false
    }
}



