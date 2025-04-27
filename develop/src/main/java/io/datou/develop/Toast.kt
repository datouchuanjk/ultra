package io.datou.develop

import android.os.Handler
import android.os.Looper
import android.widget.Toast

private val ToastMainLooperHandler by lazy {
    Handler(Looper.getMainLooper())
}

fun String.toast(duration: Int = Toast.LENGTH_SHORT) {
    if (Looper.getMainLooper().thread == Thread.currentThread()) {
        Toast.makeText(AppContext, this, duration).show()
    } else {
        ToastMainLooperHandler.post {
            Toast.makeText(AppContext, this, duration).show()
        }
    }
}
