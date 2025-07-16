package com.datouchuanjk.paging

fun <Value> PagingData<Value>.itemKey(
    key: (Value) -> Any
): (index: Int) -> Any = { index ->
    key(peek(index))
}