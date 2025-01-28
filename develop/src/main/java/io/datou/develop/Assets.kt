package io.datou.develop

import java.io.InputStream

fun <T> useAssets(fileName: String, block: (InputStream) -> T): T? {
    return try {
        App.assets.open(fileName).use(block)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun stringOfAssets(fileName: String) = useAssets(fileName) {
    it.bufferedReader()
        .use { reader ->
            reader.readText()
        }
}

