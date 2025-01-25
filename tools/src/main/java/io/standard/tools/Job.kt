package io.standard.tools

import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

inline fun buildJobSequencer(buildAction: JobSequencerBuilder.() -> Unit) =
    JobSequencerBuilder().apply(buildAction)
        .build()

class JobSequencerBuilder @PublishedApi internal constructor() {
    private val _jobs = mutableListOf<(JobSequencerHandler) -> Unit>()
    fun withJob(block: (JobSequencerHandler) -> Unit) {
        _jobs.add(block)
    }

    @PublishedApi
    internal fun build(): JobSequencer {
        return JobSequencer(_jobs)
    }
}

class JobSequencer internal constructor(private val _jobs: List<(JobSequencerHandler) -> Unit>) {
    private var _continuation: CancellableContinuation<Unit>? = null
    private var _isCancelled = false
    private var _handler = object : JobSequencerHandler {
        override fun doNext() {
            _continuation?.resume(Unit)
            _continuation = null
        }

        override fun cancel() {
            _isCancelled = true
            _continuation?.cancel()
            _continuation = null
        }
    }

    suspend fun start() {
        _jobs.forEach { job ->
            if (_isCancelled) {
                return@forEach
            }
            suspendCancellableCoroutine {
                _continuation = it
                try {
                    job(_handler)
                } catch (e: Exception) {
                    _isCancelled = true
                    _continuation?.cancel()
                    _continuation = null
                }
            }
        }
    }
}

interface JobSequencerHandler {
    fun doNext()
    fun cancel()
}






