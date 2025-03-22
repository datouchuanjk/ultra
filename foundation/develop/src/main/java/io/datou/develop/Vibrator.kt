package io.datou.develop

import android.Manifest
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.annotation.RequiresPermission
import androidx.core.content.getSystemService

@RequiresPermission(Manifest.permission.VIBRATE)
fun vibrate(duration: Long = 50) {
    val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        InstanceApp.getSystemService<VibratorManager>()?.defaultVibrator
    } else {
        InstanceApp.getSystemService<Vibrator>()
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        vibrator?.vibrate(
            VibrationEffect.createOneShot(
                duration,
                VibrationEffect.DEFAULT_AMPLITUDE
            )
        )
    } else {
        vibrator?.vibrate(duration)
    }
}
