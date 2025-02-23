package io.standard.paging3

import androidx.paging.PagingSource.LoadParams

 val DefaultNextKey: (Int) -> LoadParams<Int>.(List<Any?>) -> Int? = { initialKey ->
    {
        if (it.isEmpty() || it.size < loadSize) {
            null
        } else {
            val oldKey = key ?: initialKey
            oldKey + 1
        }
    }
}