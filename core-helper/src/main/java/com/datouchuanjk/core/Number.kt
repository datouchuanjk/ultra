package com.datouchuanjk.core

import java.text.DecimalFormat

val Number.format2f: String get() = DecimalFormat("#.##").format(toFloat())

val Number.format2fToFileSize: String
    get() {
        val units = arrayOf("B", "KB", "MB", "GB", "TB")
        var size = toDouble()
        var unitIndex = 0
        while (size >= 1024 && unitIndex < units.lastIndex) {
            size /= 1024.0
            unitIndex++
        }
        return "${size.format2f} ${units[unitIndex]}"
    }