package io.datou.develop

import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.launch

object FlowBus {
    @PublishedApi
    internal val InternalBusEvents = MutableSharedFlow<Any>()

    fun post(event: Any) {
        ProcessLifecycleOwner.get().lifecycleScope.launch {
            InternalBusEvents.emit(event)
        }
    }

    inline fun <reified T> collect(): Flow<T> {
        return InternalBusEvents.filterIsInstance<T>()
    }
}
