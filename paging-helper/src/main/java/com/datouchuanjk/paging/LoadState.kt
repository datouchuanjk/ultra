package com.datouchuanjk.paging

sealed class LoadState(
    val endOfPaginationReached: Boolean
) {
    class NotLoading(endOfPaginationReached: Boolean) : LoadState(endOfPaginationReached) {

        override fun equals(other: Any?): Boolean {
            return other is NotLoading && endOfPaginationReached == other.endOfPaginationReached
        }

        override fun toString(): String {
            return "NotLoading $endOfPaginationReached"
        }

        override fun hashCode(): Int {
            return javaClass.hashCode()
        }
    }

    data object Loading : LoadState(false) {
        override fun toString(): String {
            return "Loading"
        }
    }

    class Error(val error: Throwable) : LoadState(false) {

        override fun equals(other: Any?): Boolean {
            return other is Error && error == other.error
        }

        override fun hashCode(): Int {
            return error.hashCode()
        }

        override fun toString(): String {
            return "Error $error"
        }
    }
}