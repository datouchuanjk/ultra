package io.datou.develop

import android.util.Patterns
import android.webkit.MimeTypeMap
import java.net.MalformedURLException
import java.net.URL

val String.extension: String? get() = MimeTypeMap.getFileExtensionFromUrl(this)

val String.mimeType get() = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)

fun String.isUrl() = Patterns.WEB_URL.matcher(this).matches()

fun String.filterDigit() = filter { it.isDigit() }

fun String.filterEnglish() = filter { it in 'a'..'z' || it in 'A'..'Z' }

fun String.filterChinese() = filter { it in '\u4e00'..'\u9fa5' }

fun String.isNotNullOrEmpty() = !isNullOrEmpty()

fun String.isNotNullOrBlank() = !isNullOrBlank()


