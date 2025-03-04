package io.datou.develop

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.InputStream
import java.io.OutputStream

fun InputStream.readText() = bufferedReader()
    .use { reader ->
        reader.readText()
    }

fun InputStream.copyTo(outputStream: OutputStream, bufferSize: Int = DEFAULT_BUFFER_SIZE) = flow {
    var bytesCopied: Long = 0
    val buffer = ByteArray(bufferSize)
    var bytes = read(buffer)
    while (bytes >= 0) {
        outputStream.write(buffer, 0, bytes)
        bytesCopied += bytes
        bytes = read(buffer)
        emit(bytes)
    }
}.flowOn(Dispatchers.IO)

fun String.openAsInputStream(): InputStream {
    return App.assets.open(this)
}


