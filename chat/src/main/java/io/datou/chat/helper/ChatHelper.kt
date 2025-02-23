package io.datou.chat.helper

import com.hyphenate.EMCallBack
import com.hyphenate.EMValueCallBack
import com.hyphenate.chat.EMClient
import com.hyphenate.chat.EMConversation
import com.hyphenate.chat.EMCursorResult
import com.hyphenate.chat.EMOptions
import io.datou.chat.listener.HANDLER
import io.datou.chat.data.ChatResponse
import io.datou.chat.exception.ChatException
import io.datou.chat.listener.ChatListener
import io.datou.develop.App
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

object ChatHelper {

    fun init(key: String) {
        val emOptions = EMOptions().apply {
            appKey = key
            autoLogin = false
            requireAck = true
            requireDeliveryAck = true
        }
        EMClient.getInstance().init(App, emOptions)
    }

    suspend fun loginWithToken(
        userName: String,
        token: String,
    ) {
        suspendCancellableCoroutine { cancellableContinuation ->
            if (EMClient.getInstance().isLoggedIn) {
                logout()
            }
            EMClient.getInstance().loginWithToken(
                userName,
                token,
                object : EMCallBack {
                    override fun onSuccess() {
                        EMClient.getInstance().chatManager().apply {
                            loadAllConversations()
                            addConversationListener(HANDLER)
                            addMessageListener(HANDLER)
                            HANDLER.refreshUnreadMessageCount()
                        }
                        cancellableContinuation.resume(Unit)
                    }

                    override fun onError(code: Int, message: String?) {
                        cancellableContinuation.resumeWithException(ChatException(code, message))
                    }
                }
            )
        }
    }

    suspend fun login(
        userName: String,
        pwd: String,
    ) {
        suspendCancellableCoroutine { cancellableContinuation ->
            if (EMClient.getInstance().isLoggedIn) {
                logout()
            }
            EMClient.getInstance().login(
                userName,
                pwd,
                object : EMCallBack {
                    override fun onSuccess() {
                        EMClient.getInstance().chatManager().apply {
                            loadAllConversations()
                            addConversationListener(HANDLER)
                            addMessageListener(HANDLER)
                            HANDLER.refreshUnreadMessageCount()
                        }
                        cancellableContinuation.resume(Unit)
                    }

                    override fun onError(code: Int, message: String?) {
                        cancellableContinuation.resumeWithException(ChatException(code, message))
                    }
                }
            )
        }
    }

    fun logout() {
        try {
            EMClient.getInstance().logout(true)
        } catch (e: Exception) {
            try {
                EMClient.getInstance().logout(false)
            } catch (e: Exception) {
                throw ChatException(e)
            }
        } finally {
            EMClient.getInstance().chatManager().apply {
                cleanConversationsMemoryCache()
                HANDLER.refreshUnreadMessageCount()
                removeConversationListener(HANDLER)
                removeMessageListener(HANDLER)
            }
        }
    }

    suspend fun getConversationsFromDB() = suspendCancellableCoroutine { cancellableContinuation ->
        EMClient.getInstance().chatManager()
            .asyncFilterConversationsFromDB(
                { true },
                false,
                object : EMValueCallBack<List<EMConversation?>?> {
                    override fun onSuccess(result: List<EMConversation?>?) {
                        cancellableContinuation.resume(
                            ChatResponse(
                                data = result?.filterNotNull() ?: emptyList(),
                                cursor = null,
                                isNoMoreData = true
                            )
                        )
                    }

                    override fun onError(error: Int, errorMsg: String) {
                        cancellableContinuation.resumeWithException(
                            ChatException(
                                error,
                                errorMsg
                            )
                        )
                    }
                })
    }

    suspend fun getConversationsFromService(
        cursor: String? = null,
        limit: Int = 20
    ) = suspendCancellableCoroutine { cancellableContinuation ->
        EMClient.getInstance().chatManager().asyncFetchConversationsFromServer(
            limit,
            cursor,
            object : EMValueCallBack<EMCursorResult<EMConversation>> {
                override fun onSuccess(result: EMCursorResult<EMConversation>?) {
                    cancellableContinuation.resume(
                        ChatResponse(
                            data = result?.data?.filterNotNull() ?: emptyList(),
                            cursor = result?.cursor,
                            isNoMoreData = (result?.data?.size ?: 0) < limit
                        )
                    )
                }

                override fun onError(code: Int, message: String?) {
                    cancellableContinuation.resumeWithException(
                        ChatException(
                            code,
                            message
                        )
                    )
                }
            })
    }

    fun addListener(listener: ChatListener) {
        HANDLER.add(listener)
    }

    fun removeListener(listener: ChatListener) {
        HANDLER.remove(listener)
    }
}