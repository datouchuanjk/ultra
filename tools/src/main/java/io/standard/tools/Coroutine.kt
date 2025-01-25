package io.standard.tools

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

val SilentExceptionHandler = CoroutineExceptionHandler { _, exception ->
    exception.printStackTrace()
}

fun CoroutineScope.launchSilently(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> Unit
) = launch(
    context = context + SilentExceptionHandler,
    start = start,
    block = block
)


