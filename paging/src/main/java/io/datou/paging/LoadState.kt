package io.datou.paging


sealed class LoadState(val endOfPaginationReached: Boolean) {
    class NotLoading(endOfPaginationReached: Boolean) : LoadState(endOfPaginationReached)
    data object Loading : LoadState(false)
    data class Error(val cause: Throwable) : LoadState(false)
}