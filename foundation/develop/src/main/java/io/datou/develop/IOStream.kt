package io.datou.develop

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.io.InputStream
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL

fun InputStream.useCopyToWithProgress(outputStream: OutputStream) = flow {
    var bytesCopied: Long = 0
    val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
    use { input ->
        var bytes = input.read(buffer)
        outputStream.use { output ->
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
fun String.useCopyToWithProgress(outputStream: OutputStream) = flow {
    val it = toHttpURLConnection()
    it.requestMethod = "GET"
    it.connect()
    emit(it)
}.flatMapConcat {
    val totalBytes = it.contentLength.takeIf { it >= 0 }
        ?.toLong()
        ?: throw NullPointerException("contentLength < 0")
    it.inputStream.useCopyToWithProgress(outputStream)
        .map {
            (it.toDouble() / totalBytes).toFloat().format2f
        }
}

fun String.toHttpURLConnection() = (URL(this).openConnection() as HttpURLConnection)

fun InputStream.readText() = bufferedReader()
    .use { reader ->
        reader.readText()
    }


