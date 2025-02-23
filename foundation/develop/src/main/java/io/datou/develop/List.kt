package io.datou.develop

fun <T> MutableList<T>.findIndex(block: (T) -> Boolean): Int? {
    return withIndex().find {
        block(it.value)
    }?.index
}

fun <T> MutableList<T>.reinsertIf(newItem: T, block: (T) -> Boolean) {
    findIndex(block)?.let {
        removeAt(it)
        add(it, newItem)
    }
}

fun <T> MutableList<T>.reinsertToFirstIf(newItem: T, block: (T) -> Boolean) {
    findIndex(block)?.let {
        removeAt(it)
        add(0, newItem)
    }
}

fun <T> MutableList<T>.reinsertToLastIf(newItem: T, block: (T) -> Boolean) {
    findIndex(block)?.let {
        removeAt(it)
        add(newItem)
    }
}

