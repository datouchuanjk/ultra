package io.datou.develop

import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

inline fun buildQuestStack(buildAction: QuestStackBuilder.() -> Unit) =
    QuestStackBuilder().apply(buildAction)
        .build()

class QuestStackBuilder @PublishedApi internal constructor() {
    private val _quests = mutableListOf<(QuestStackHandler) -> Unit>()
    fun addQuest(block: (QuestStackHandler) -> Unit) {
        _quests.add(block)
    }

    @PublishedApi
    internal fun build(): QuestStack {
        return QuestStack(_quests)
    }
}

class QuestStack internal constructor(private val _quests: List<(QuestStackHandler) -> Unit>) {
    private var _continuation: CancellableContinuation<Unit>? = null
    private var _isCancelled = false
    private var _handler = object : QuestStackHandler {
        override fun next() {
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
        _quests.forEach { job ->
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

interface QuestStackHandler {
    fun next()
    fun cancel()
}






