package io.datou.chat.helper

import com.hyphenate.EMValueCallBack
import com.hyphenate.chat.EMClient
import com.hyphenate.chat.EMConversation
import com.hyphenate.chat.EMCursorResult
import io.datou.chat.util.IMException
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * 会话帮助类
 */
class ConversationHelper {

    private val _limit = 20
    private var _cursor: String? = null
    private var _isNoMore: Boolean = false
    val isNoMore get() = _isNoMore

    suspend fun refresh(): List<EMConversation> {
        _isNoMore = false
        return if (IMHelper.isFirstLogin()) {
            fromService()
        } else {
            fromDB()
        }
    }

    suspend fun loadMore(): List<EMConversation> {
        if (_isNoMore) {
            return emptyList()
        }
        return refresh()
    }

    private suspend fun fromDB(): List<EMConversation> {
        return suspendCancellableCoroutine { cancellableContinuation ->
            EMClient.getInstance().chatManager()
                .asyncFilterConversationsFromDB(
                    { true },
                    false,
                    object : EMValueCallBack<List<EMConversation?>?> {
                        override fun onSuccess(result: List<EMConversation?>?) {
                            val list = result
                                ?.filterNotNull()
                                ?: emptyList()
                            _isNoMore = true
                            cancellableContinuation.resume(list)
                        }

                        override fun onError(error: Int, errorMsg: String) {
                            cancellableContinuation.resumeWithException(
                                IMException(
                                    error,
                                    errorMsg
                                )
                            )
                        }
                    })
        }
    }

    private suspend fun fromService(): List<EMConversation> {
        return suspendCancellableCoroutine { cancellableContinuation ->
            EMClient.getInstance().chatManager().asyncFetchConversationsFromServer(
                _limit,
                _cursor,
                object : EMValueCallBack<EMCursorResult<EMConversation>> {
                    override fun onSuccess(result: EMCursorResult<EMConversation>?) {
                        _cursor = result?.cursor
                        _isNoMore = (result?.data?.size ?: 0) < _limit
                        val list = result?.data
                            ?.filterNotNull()
                            ?: emptyList()
                        cancellableContinuation.resume(list)
                    }

                    override fun onError(code: Int, message: String?) {
                        cancellableContinuation.resumeWithException(
                            IMException(
                                code,
                                message
                            )
                        )
                    }
                })
        }
    }
}
