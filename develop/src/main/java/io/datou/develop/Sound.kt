package io.datou.develop

import android.media.MediaPlayer
import android.media.RingtoneManager

fun playSystemDefaultSound() {
    val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
    try {
        MediaPlayer.create(AppContext, soundUri).apply {
            setOnCompletionListener { mp ->
                mp.release()
            }
            start()
        }
    } catch (e: Exception) {
        e.printStackTrace()
        RingtoneManager.getRingtone(AppContext, soundUri)?.play()
    }
}