package io.standard.tools

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.sample
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL

@OptIn(FlowPreview::class)
fun downloadUrlToFile(url: String, file: File) = flow<DownloadResult> {
    val connection = URL(url).openConnection() as HttpURLConnection
    connection.requestMethod = "GET"
    connection.connect()
    val totalBytes = connection.contentLength.takeIf { it >= 0 }?.toLong() ?: -1L
    var downloadedBytes = 0L
    connection.inputStream.use { inputStream ->
        FileOutputStream(file).use { outputStream ->
            val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
            var bytesRead: Int
            while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                outputStream.write(buffer, 0, bytesRead)
                downloadedBytes += bytesRead
                val progress = if (totalBytes > 0) {
                    ((downloadedBytes.toDouble() / totalBytes) * 100).toInt()
                } else {
                    -1
                }
                emit(DownloadResult.Progress(progress))
            }
        }
    }
}.flowOn(Dispatchers.IO)
    .distinctUntilChanged()
    .take(50)
    .sample(1000)
    .onStart {
        emit(DownloadResult.Start)
    }.onCompletion {
        emit(DownloadResult.Success(file))
    }.catch {
        emit(DownloadResult.Error(it))
    }

sealed class DownloadResult {
    data object Start : DownloadResult()
    data class Progress(val progress: Int) : DownloadResult()
    data class Success(val file: File) : DownloadResult()
    data class Error(val exception: Throwable) : DownloadResult()
}
