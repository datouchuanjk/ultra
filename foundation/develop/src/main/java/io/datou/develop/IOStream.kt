package io.datou.develop

import androidx.compose.runtime.collectAsState
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



