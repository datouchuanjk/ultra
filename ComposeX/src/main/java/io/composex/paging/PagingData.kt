package io.composex.paging

interface PagingData<Value> {
    val appendState: LoadState
    val refreshState: LoadState
    val isFirstRefresh: Boolean
    val count: Int
    fun refresh()
    fun retry()
    operator fun get(index: Int): Value
    fun peek(index: Int): Value
    fun itemKey(key: (Value) -> Any): (index: Int) -> Any
    fun update(block: (MutableList<Value>) -> Unit)
}