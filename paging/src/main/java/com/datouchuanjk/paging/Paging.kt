package com.datouchuanjk.paging

import kotlinx.coroutines.flow.StateFlow

interface Paging<Value>  {
    val flow: StateFlow<PagingData<Value>>
}