package io.datou.develop

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.launch

@PublishedApi
internal val InternalBusEvents = MutableSharedFlow<Any>()

fun postEvent(event: Any) {
    AppScope.launch {
        InternalBusEvents.emit(event)
    }
}

inline fun <reified T> subscribeEvent(): Flow<T> {
    return InternalBusEvents.filterIsInstance<T>()
}
