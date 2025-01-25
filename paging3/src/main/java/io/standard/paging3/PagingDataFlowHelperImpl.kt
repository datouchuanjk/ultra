package io.standard.paging3

import androidx.paging.PagingData
import androidx.paging.filter
import androidx.paging.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine

internal class PagingDataFlowHelperImpl<T : Any>(
    source: Flow<PagingData<T>>
) : PagingDataFlowHelper<T> {

    private val _tasks = mutableMapOf<String, (PagingData<T>) -> PagingData<T>>()
    private val _refresh = MutableStateFlow(0)
    private var _cachePagingData: PagingData<T>? = null

    override val flow = source.combine(_refresh) { pagingData, _ ->
        if (_cachePagingData != pagingData) {
            _cachePagingData = pagingData
            _tasks.clear()
        }
        var newPagingData = pagingData
        _tasks.values.forEach {
            newPagingData = it(newPagingData)
        }
        newPagingData
    }

    override fun delete(tag: String, predicate: (T) -> Boolean) {
        _tasks[tag] = { it ->
            it.filter {
                !predicate(it)
            }
        }
        _refresh.value++
    }

    override fun update(tag: String, transform: (T) -> T) {
        _tasks[tag] = {
            it.map(transform)
        }
        _refresh.value++
    }
}
