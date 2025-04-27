package io.datou.develop

import kotlinx.coroutines.flow.flow
import java.io.InputStream
import java.io.OutputStream

@Suppress("BlockingMethodInNonBlockingContext")
fun InputStream.copyAsFlow(outputStream: OutputStream) = flow {
    var bytesCopied: Long = 0
    val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
    var bytes = read(/* b = */ buffer)
    while (bytes >= 0) {
        outputStream.write(buffer, 0, bytes)
        bytesCopied += bytes
        bytes = read(buffer)
        emit(bytesCopied)
    }
}

fun InputStream.readAsString() = bufferedReader()
    .use { reader ->
        reader.readText()
    }




