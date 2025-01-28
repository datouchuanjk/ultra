package io.datou.develop

import java.text.DecimalFormat

fun Number.format2f(): String {
    val decimalFormat = DecimalFormat("#.##")
    return decimalFormat.format(this)
}

fun Number.formatFileSize(): String {
    val units = arrayOf("B", "KB", "MB", "GB", "TB")
    var size = this.toDouble()
    var unitIndex = 0
    while (size >= 1024 && unitIndex < units.size - 1) {
        size /= 1024.0
        unitIndex++
    }
    return "${size.format2f()} ${units[unitIndex]}"
}