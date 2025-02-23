package io.datou.develop

fun <T> MutableList<T>.findIndex(block: (T) -> Boolean): Int? {
    return withIndex().find {
        block(it.value)
    }?.index
}

fun <T> MutableList<T>.newIf(newItem: T, block: (T) -> Boolean) {
    findIndex(block)?.let {
        removeAt(it)
        add(it, newItem)
    }
}

