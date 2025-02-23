package io.datou.develop

import android.os.Handler
import android.os.Looper
import android.widget.Toast

internal val InternalHandler = Handler(Looper.getMainLooper())

fun toast(message: String?, duration: Int = Toast.LENGTH_SHORT) {
    message ?: return
    if ( Looper.getMainLooper().thread == Thread.currentThread()) {
        Toast.makeText(App, message, duration).show()
    } else {
        InternalHandler.post {
            Toast.makeText(App, message, duration).show()
        }
    }
}
