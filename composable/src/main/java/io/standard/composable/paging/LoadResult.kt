package io.standard.composable.paging

sealed class LoadResult<T, R> {
    class Result<T, R>(val list: List<R>, val nextKey: T?) : LoadResult<T, R>()
    class Error<T, R>(val cause: Throwable) : LoadResult<T, R>()
}

fun <T, R> List<R>.withNextKey(nextKey: () -> T?): LoadResult<T, R> {
    return LoadResult.Result(this, nextKey())
}