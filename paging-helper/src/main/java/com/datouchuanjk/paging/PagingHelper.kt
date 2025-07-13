package com.datouchuanjk.paging

import kotlinx.coroutines.flow.StateFlow

interface PagingHelper<Value>  {
    val flow: StateFlow<PagingData<Value>>
}