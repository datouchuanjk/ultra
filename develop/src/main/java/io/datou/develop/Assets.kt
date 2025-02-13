package io.datou.develop

import java.io.InputStream

fun <T> useAssets(fileName: String, block: (InputStream) -> T): T? {
    return App.assets.open(fileName).use(block)
}

fun stringOfAssets(fileName: String) = useAssets(fileName) {
    it.bufferedReader()
        .use { reader ->
            reader.readText()
        }
}

