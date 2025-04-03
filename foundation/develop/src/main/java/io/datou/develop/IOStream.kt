package io.datou.develop

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import java.io.InputStream
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL

@Suppress("BlockingMethodInNonBlockingContext")
fun InputStream.copyAsFlow(outputStream: OutputStream) = flow {
    var bytesCopied: Long = 0
    val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
    var bytes = read(/* b = */ buffer)
    while (bytes >= 0) {
        outputStream.write(buffer, 0, bytes)
        bytesCopied += bytes
        bytes = read(buffer)
        emit(bytes)
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
fun String.copyAsFlow(outputStream: OutputStream) = flowOf(openAsHttpURLConnection())
    .map {
        if (it == null) {
            throw NullPointerException()
        }
        it
    }.flatMapConcat { connection ->
        val totalBytes = connection.contentLength.takeIf { it >= 0 }
            ?.toLong()
            ?: throw NullPointerException("contentLength < 0")
        connection.inputStream.use { input ->
            input.copyAsFlow(outputStream)
        }.map {
            (it.toDouble() / totalBytes).toFloat().format2f
        }
    }

fun InputStream.readText() = bufferedReader()
    .use { reader ->
        reader.readText()
    }

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



