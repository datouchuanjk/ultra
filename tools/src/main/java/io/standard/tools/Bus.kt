package io.standard.tools

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.launch

@PublishedApi
internal val BusEvents = MutableSharedFlow<Any>()

fun postEvent(event: Any) {
    AppScope.launch {
        BusEvents.emit(event)
    }
}

inline fun <reified T> subscribeEvent(): Flow<T> {
    return BusEvents.filterIsInstance<T>()
}
