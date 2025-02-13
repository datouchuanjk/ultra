package io.datou.chat.util

import android.util.Log

object IMLog {
    private const val DEBUG = true
    fun e(message: String?) {
        if (DEBUG) {
            message?.let { Log.e("IMLog", it) }
        }
    }
}