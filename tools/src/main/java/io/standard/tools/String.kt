package io.standard.tools

import android.util.Patterns
import java.net.MalformedURLException
import java.net.URL

val String.Companion.Space get() = " "

val String.Companion.Tab get() = "  "

fun String.isUrl(): Boolean {
    return try {
        URL(this)
        Patterns.WEB_URL.matcher(this).matches()
    } catch (e: MalformedURLException) {
        false
    }
}

fun String.filterDigits(): String {
    return filter { it.isDigit() }
}

fun String.filterEnglish(): String {
    return filter { it in 'a'..'z' || it in 'A'..'Z' }
}

fun String.filterChinese() = filter {
    it in '\u4e00'..'\u9fa5'
}

