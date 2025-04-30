package io.datou.develop

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch


fun LifecycleOwner.launchWhenResumed(block: () -> Unit): Job {
    return if (lifecycle.currentState == Lifecycle.State.RESUMED) {
        lifecycleScope.launch { block()}
    } else {
        lifecycleScope.launch {
            lifecycle.currentStateFlow
                .filter { it == Lifecycle.State.RESUMED }
                .take(1)
                .collect {
                    block()
                }
        }
    }
}