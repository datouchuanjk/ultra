package com.datouchuanjk.paging

import java.util.Collections

internal data class PagingDataImpl<Value>(
    val data: List<Value>,
    override val isFirstRefresh: Boolean,
    override val appendState: LoadState,
    override val refreshState: LoadState,
    val callback: Callback<Value>
) : PagingData<Value> {
    interface Callback<Value> {
        fun onRefresh()
        fun onRetry()
        fun onLoad(index: Int, count: Int)
        fun onUpdate(list: List<Value>)
    }

    override val count = data.count()

    override operator fun get(index: Int): Value {
        if (count > 0) {
            callback.onLoad(index, count)
        }
        return peek(index)
    }

    override fun peek(index: Int): Value {
        return data[index]
    }

    override fun itemKey(key: (Value) -> Any): (Int) -> Any {
        return { index ->
            key(data[index])
        }
    }

    override fun update(block: (MutableList<Value>) -> Unit) {
        val list = ArrayList(data)
        block(list)
        callback.onUpdate(Collections.unmodifiableList(list))
    }

    override fun refresh() {
        callback.onRefresh()
    }

    override fun retry() {
        callback.onRetry()
    }
}