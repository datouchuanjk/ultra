package io.standard.paging3

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

fun <T : Any> Flow<PagingData<T>>.asHelper(): PagingDataFlowHelper<T> {
    return PagingDataFlowHelperImpl(this)
}

interface PagingDataFlowHelper<T : Any> {

    val flow: Flow<PagingData<T>>

    fun delete(tag: String, predicate: (T) -> Boolean)

    fun update(tag: String, transform: (T) -> T)
}
