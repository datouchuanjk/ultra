package io.datou.develop

import androidx.annotation.WorkerThread
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import java.io.InputStream
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL

@WorkerThread
fun InputStream.useCopyAsFlow(outputStream: OutputStream) = flow {
    var bytesCopied: Long = 0
    val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
    use { input ->
        outputStream.use { output ->
            var bytes = input.read(buffer)
            while (bytes >= 0) {
                output.write(buffer, 0, bytes)
                bytesCopied += bytes
                bytes = input.read(buffer)
                emit(bytes)
            }
        }
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
fun String.useCopyAsFlow(outputStream: OutputStream) = flowOf(openAsConnection())
    .map {
        if (it == null) {
            throw NullPointerException()
        }
        it
    }.flatMapConcat {
        val totalBytes = it.contentLength.takeIf { it >= 0 }
            ?.toLong()
            ?: throw NullPointerException("contentLength < 0")
        it.inputStream.useCopyAsFlow(outputStream)
            .map {
                (it.toDouble() / totalBytes).toFloat().format2f
            }
    }

fun InputStream.readText() = bufferedReader()
    .use { reader ->
        reader.readText()
    }

fun String.openAsConnection() = try {
    if (isEmpty()) {
        null
    } else {
        URL(this).openConnection() as HttpURLConnection
    }
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



