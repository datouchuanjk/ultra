package io.standard.composable.paging

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

internal fun <T, R> buildMutablePagingController(
    scope: CoroutineScope,
    context: CoroutineContext = Dispatchers.IO,
    initialKey: T?,
    config: LoadConfig = DefaultLoadConfig,
    load: suspend (LoadParams<T>) -> LoadResult<T, R>
): PagingController<R> {
    return MutablePagingController(
        scope = scope,
        context = context,
        initialKey = initialKey,
        config = config,
        load = load
    )
}

internal class MutablePagingController<T, R>(
    private val scope: CoroutineScope,
    private val context: CoroutineContext,
    private val initialKey: T?,
    private val config: LoadConfig,
    private val load: suspend (LoadParams<T>) -> LoadResult<T, R>
) : PagingController<R> {

    private val _data = mutableStateListOf<R>()
    private var _currentKey = initialKey
    private var _nextKey = initialKey
    override val count get() = _data.size
    override fun get(index: Int) = _data[index]
    override var refreshState by mutableStateOf<LoadState>(LoadState.NotLoading(false))
    override var loadMoreState by mutableStateOf<LoadState>(LoadState.NotLoading(false))

    init {
        refresh()
    }

    override fun refresh() {
        if (refreshState is LoadState.Loading) {
            return
        }
        _currentKey = initialKey
        handle(true) {
            refreshState = it
        }
    }

    override fun loadMore() {
        if (loadMoreState is LoadState.NotLoading
            && !loadMoreState.endOfPaginationReached
            && count > 0
        ) {
            _currentKey = _nextKey
            handle {
                loadMoreState = it
            }
        }
    }

    override fun retry() {
        if (loadMoreState is Error) {
            handle {
                loadMoreState = it
            }
        }
    }

    private fun handle(isRefresh: Boolean = false, block: (LoadState) -> Unit) {
        scope.launch {
            block(LoadState.Loading)
            withContext(context) {
                val loadParams = LoadParams(
                    key = _currentKey,
                    config = config,
                )
                when (val result = load(loadParams)) {
                    is LoadResult.Result -> {
                        if (isRefresh) {
                            _data.clear()
                        }
                        _data.addAll(result.list)
                        _nextKey = result.nextKey
                    }

                    is LoadResult.Error -> {
                        throw result.cause
                    }
                }
            }
        }.invokeOnCompletion {
            if (it == null) {
                block(LoadState.NotLoading(_nextKey == null))
            } else {
                block(LoadState.Error(it))
            }
        }
    }
}