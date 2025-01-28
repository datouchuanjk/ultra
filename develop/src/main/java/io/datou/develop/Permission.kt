package io.datou.develop

import android.content.pm.PackageManager.PERMISSION_GRANTED
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale as S
import androidx.core.content.ContextCompat.checkSelfPermission as C

fun isPermissionGranted(vararg name: String): Boolean {
    return name.all {
        C(App, it) == PERMISSION_GRANTED
    }
}

fun isPermissionPermanentlyDenied(vararg name: String): Boolean {
    return name.all {
        !isPermissionGranted(it) && PeekActivity?.let { activity ->
            !S(activity, it)
        } ?: false
    }
}



