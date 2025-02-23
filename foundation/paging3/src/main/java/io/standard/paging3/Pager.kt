package io.standard.paging3

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingSource.LoadParams
import androidx.paging.PagingState

fun <K : Any, V : Any> buildPager(
    initialKey: K? = null,
    config: PagingConfig = PagingConfig(
        pageSize = 20,
        prefetchDistance = 1,
        initialLoadSize = 20,
    ),
    refreshKey: (PagingState<K, V>) -> K? = { initialKey },
    prevKey: LoadParams<K>.(List<V>) -> K? = { null },
    nextKey: LoadParams<K>.(List<V>) -> K? = { null },
    load: suspend LoadParams<K>.() -> List<V>
) = Pager(
    initialKey = initialKey,
    config = config,
    pagingSourceFactory = {
        object : PagingSource<K, V>() {
            override fun getRefreshKey(state: PagingState<K, V>): K? {
                return refreshKey.invoke(state)
            }

            override suspend fun load(params: LoadParams<K>): LoadResult<K, V> {
                return try {
                    load.invoke(params).run {
                        LoadResult.Page(
                            data = this,
                            prevKey = prevKey.invoke(params, this),
                            nextKey = nextKey.invoke(params, this)
                        )
                    }
                } catch (e: Exception) {
                    LoadResult.Error(e)
                }
            }
        }
    }
)
