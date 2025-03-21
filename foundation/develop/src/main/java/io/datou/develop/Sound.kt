package io.datou.develop

import android.media.MediaPlayer
import android.media.RingtoneManager

fun playSystemDefaultSound() {
    val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
    try {
        MediaPlayer.create(InstanceApp, soundUri).apply {
            setOnCompletionListener { mp ->
                mp.release()
            }
            start()
        }
    } catch (e: Exception) {
        e.printStackTrace()
        RingtoneManager.getRingtone(InstanceApp, soundUri)?.play()
    }
}