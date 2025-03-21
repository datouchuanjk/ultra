package io.datou.develop

import java.text.DecimalFormat

val Number.format2f: Float get() = DecimalFormat("#.##").format(this).toFloat()

val Number.format2fFileSize: String
    get() {
        val units = arrayOf("B", "KB", "MB", "GB", "TB")
        var size = toDouble()
        var unitIndex = 0
        while (size >= 1024 && unitIndex < units.size - 1) {
            size /= 1024.0
            unitIndex++
        }
        return "${size.format2f} ${units[unitIndex]}"
    }