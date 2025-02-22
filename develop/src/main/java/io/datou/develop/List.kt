package io.datou.develop

fun <T> MutableList<T>.findIndex(block: (T) -> Boolean): Int? {
    return withIndex().find {
        block(it.value)
    }?.index
}

fun <T> MutableList<T>.replaceIf(newItem: T, block: (T) -> Boolean) {
    findIndex(block)?.let {
        this[it] = newItem
    }
}

fun <T> MutableList<T>.addOrReplaceIf(newItem: T, block: (T) -> Boolean) {
    findIndex(block)?.let {
        this[it] = newItem
    } ?: add(newItem)
}
fun <T> MutableList<T>.addIndexOrReplaceIf(index:Int =0,newItem: T, block: (T) -> Boolean) {
    findIndex(block)?.let {
        this[it] = newItem
    } ?: add(index,newItem)
}

fun <T> MutableList<T>.removeIf(block: (T) -> Boolean) {
    findIndex(block)?.let {
        removeAt(it)
    }
}