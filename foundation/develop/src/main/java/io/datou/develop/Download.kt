package io.datou.develop

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL

fun downloadUrlToFile(url: String, file: File) = flow<DownloadResult> {
    val connection = URL(url).openConnection() as HttpURLConnection
    connection.requestMethod = "GET"
    connection.connect()
    val totalBytes = connection.contentLength.takeIf { it >= 0 }?.toLong()
        ?: throw NullPointerException("contentLength<0")
    var downloadedBytes = 0L
    connection.inputStream.use { inputStream ->
        FileOutputStream(file).use { outputStream ->
            val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
            var bytesRead: Int
            while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                outputStream.write(buffer, 0, bytesRead)
                downloadedBytes += bytesRead
                val progress = ((downloadedBytes.toDouble() / totalBytes)).toFloat().format2f()
                emit(DownloadResult.Progress(progress))
            }
        }
    }
}.flowOn(Dispatchers.IO)
    .distinctUntilChanged()
    .onStart {
        emit(DownloadResult.Start)
    }.onCompletion {
        emit(DownloadResult.Completion(file))
    }.catch {
        emit(DownloadResult.Catch(it))
    }

suspend fun Flow<DownloadResult>.collectOnlyCompletion(
    action: suspend (value: DownloadResult.Completion) -> Unit
) = collectLatest {
    when (it) {
        is DownloadResult.Start -> {}
        is DownloadResult.Progress -> {}
        is DownloadResult.Catch -> {
            throw it.exception
        }

        is DownloadResult.Completion -> {
            action(it)
        }
    }
}

sealed class DownloadResult {
    data object Start : DownloadResult()
    data class Progress(val progress: Float) : DownloadResult()
    data class Completion(val file: File) : DownloadResult()
    data class Catch(val exception: Throwable) : DownloadResult()
}