package io.composex.util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.InputStream
import java.io.OutputStream

fun InputStream.copyToAsFlow(outputStream: OutputStream) = flow {
    var bytesCopied: Long = 0
    val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
    var bytes = read(buffer)
    while (bytes >= 0) {
        outputStream.write(buffer, 0, bytes)
        bytesCopied += bytes
        bytes = read(buffer)
        emit(bytesCopied)
    }
}.flowOn(Dispatchers.IO)

fun InputStream.readText() = bufferedReader()
    .use { reader ->
        reader.readText()
    }
