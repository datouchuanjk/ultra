package com.module.basic.http

import okhttp3.FormBody
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okio.Buffer
import java.nio.charset.Charset

/**
 * 这个只是用于输入
 */
class LogInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().apply { logRequest(this) }
        val response = chain.proceed(request).apply { logResponse(this) }
        return response
    }

    private fun logRequest(request: Request) {
        val url = request.url.toString()
        val headers = request.headers.names().map {
            it to request.headers[it]
        }
        val method = request.method
        when (method) {
            "get" -> {
                request.url.queryParameterNames.map {
                    it to request.url.queryParameter(it)
                }
            }

            "post" -> {
                val requestBody = request.body
                if (requestBody is FormBody) {
                    for (i in 0..<requestBody.size) {
                        requestBody.name(i) to requestBody.value(i)
                    }
                } else {
                    var charset = Charset.forName("UTF-8")
                    val contentType = requestBody?.contentType()
                    if (contentType != null) {
                        charset = contentType.charset(charset)
                    }
                    val buffer = Buffer()
                    requestBody?.writeTo(buffer)
                    val p = buffer.readString(charset)
                }
            }
        }
    }

    private fun logResponse(response: Response) {
        val url = response.request.url
        val responseBody = response.body
        val source = responseBody?.source()
        source?.request(Long.MAX_VALUE) // Buffer the entire body.
        var charset = Charset.forName("UTF-8")
        val contentType = responseBody?.contentType()
        if (contentType != null) {
            charset = contentType.charset(charset)
        }
        val responseData = source?.buffer?.clone()?.readString(charset)
    }
}