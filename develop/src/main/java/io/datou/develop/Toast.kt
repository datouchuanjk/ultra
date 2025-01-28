package io.datou.develop

import android.widget.Toast

fun toast(message: String?, duration: Int = Toast.LENGTH_SHORT) {
    message ?: return
    Toast.makeText(App, message, duration).show()
}
