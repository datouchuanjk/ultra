package io.composex.paging

import io.composex.paging.PagingDataImpl.Callback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

internal class PagingImpl<Key, Value>(
    private val initialKey: Key? = null,
    private val config: PagingConfig = PagingConfig(),
    private val load: suspend (LoadParams<Key>) -> LoadResult<Key, Value>,
    private val scope: CoroutineScope,
) : Paging<Value>, CoroutineScope by scope, Callback<Value> {

    override val flow = MutableStateFlow(
        PagingDataImpl(
            data = listOf(),
            isFirstRefresh = true,
            appendState = LoadState.NotLoading(false),
            refreshState = LoadState.NotLoading(false),
            callback = this
        )
    )
    private var _nextKey: Key? = initialKey
    private val _refreshMutex = Mutex()
    private val _appendMutex = Mutex()
    private var _refreshJob: Job? = null
    private var _appendJob: Job? = null

    init {
        refresh()
    }

    private fun refresh() {
        if (flow.value.refreshState is LoadState.Loading) {
            return
        }
        if (_refreshJob?.isActive == true) return
        if (_refreshMutex.isLocked) return
        _appendJob?.cancel()
        _refreshJob = launch {
            _refreshMutex.withLock {
                try {
                    flow.update {
                        it.copy(
                            refreshState = LoadState.Loading,
                            appendState = LoadState.NotLoading(false)
                        )
                    }
                    val result = withContext(Dispatchers.IO) {
                        load(
                            LoadParams(
                                key = initialKey,
                                pageSize = config.pageSize
                            )
                        )
                    }
                    _nextKey = result.nextKey
                    flow.update {
                        it.copy(
                            isFirstRefresh = false,
                            refreshState = LoadState.NotLoading(_nextKey == null),
                            data = result.data,
                        )
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    flow.update {
                        it.copy(refreshState = LoadState.Error(e))
                    }
                }
            }
        }
    }

    private fun load() {
        if (flow.value.refreshState is LoadState.Loading) {
            return
        }
        if (flow.value.appendState is LoadState.Loading) {
            return
        }
        if (flow.value.appendState == LoadState.NotLoading(true)) {
            return
        }
        if (flow.value.appendState is LoadState.Error) {
            return
        }
        if (_appendJob?.isActive == true) return
        if (_appendMutex.isLocked) return
        _appendJob = launch {
            _appendMutex.withLock {
                try {
                    flow.update {
                        it.copy(appendState = LoadState.Loading)
                    }
                    val result = withContext(Dispatchers.IO) {
                        load(
                            LoadParams(
                                key = _nextKey,
                                pageSize = config.pageSize
                            )
                        )
                    }
                    _nextKey = result.nextKey
                    flow.update {
                        it.copy(
                            appendState = LoadState.NotLoading(_nextKey == null),
                            data = if (it.data.isEmpty()) {
                                result.data
                            } else {
                                ArrayList(it.data).apply { addAll(result.data) }
                            }
                        )
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    flow.update {
                        it.copy(appendState = LoadState.Error(e))
                    }
                }
            }
        }
    }

    override fun onRefresh() {
        refresh()
    }

    override fun onRetry() {
        if (flow.value.refreshState is LoadState.Loading) {
            return
        }
        if (flow.value.appendState is LoadState.Error) {
            flow.update {
                it.copy(appendState = LoadState.NotLoading(false))
            }
            load()
        }
    }

    override fun onLoad(index: Int, count: Int) {
        if (index + config.prefetchDistance >= count) {
            load()
        }
    }

    override fun onUpdate(list: List<Value>) {
        flow.update {
            it.copy(data = list)
        }
    }
}