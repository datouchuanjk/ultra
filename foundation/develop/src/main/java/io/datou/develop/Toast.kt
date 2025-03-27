package io.datou.develop

import android.os.Handler
import android.os.Looper
import android.widget.Toast

fun String.toast(duration: Int = Toast.LENGTH_SHORT) {
    if (Looper.getMainLooper().thread == Thread.currentThread()) {
        Toast.makeText(Instance, this, duration).show()
    } else {
        Handler(Looper.getMainLooper()).post {
            Toast.makeText(Instance, this, duration).show()
        }
    }
}
