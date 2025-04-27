package io.datou.develop

import java.net.HttpURLConnection
import java.net.URL

fun String.openAsHttpURLConnection() = try {
    URL(this).openConnection() as HttpURLConnection
} catch (e: Exception) {
    e.printStackTrace()
    null
}

fun HttpURLConnection.checkConnection() = try {
    connect()
    responseCode == 200
} catch (e: Exception) {
    e.printStackTrace()
    false
}

inline fun <T> HttpURLConnection.use(block: (HttpURLConnection) -> T) = try {
    block(this)
} finally {
    disconnect()
}