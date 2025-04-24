package io.datou.develop

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.suspendCoroutine

val IgnoreExceptionCoroutineContext = CoroutineExceptionHandler { _, exception ->
    exception.printStackTrace()
},
fun CoroutineScope.launchSilently(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> Unit
): Job {
    return launch(
        context = context + IgnoreExceptionCoroutineContext,
        start = start,
        block = block
    )
}

fun CoroutineScope.asyncSilently(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> Unit
): Job {
    return async(
        context = context + IgnoreExceptionCoroutineContext,
        start = start,
        block = block
    )
}



