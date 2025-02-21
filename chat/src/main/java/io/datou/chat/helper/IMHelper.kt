package io.datou.chat.helper

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.hyphenate.EMCallBack
import com.hyphenate.chat.EMClient
import com.hyphenate.chat.EMOptions
import io.datou.chat.listener.ConversationListener
import io.datou.chat.listener.MessageListener
import io.datou.chat.util.IMException
import io.datou.develop.App
import io.datou.develop.SP
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

object IMHelper {

    fun init(key: String) {
        val emOptions = EMOptions().apply {
            appKey = key
            autoLogin = false
            isIncludeSendMessageInMessageListener = false
        }
        EMClient.getInstance().init(App, emOptions)
    }

    internal fun isFirstLogin(): Boolean {
        return SP.getBoolean("${EMClient.getInstance().currentUser}_first_login", true)
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
                            addConversationListener(ConversationListener.singleListener)
                            addMessageListener(ConversationListener.singleListener)
                            addMessageListener(MessageListener.singleListener)
                            SP.put("${userName}_first_login", false)
                        }
                        cancellableContinuation.resume(Unit)
                    }

                    override fun onError(code: Int, message: String?) {
                        cancellableContinuation.resumeWithException(IMException(code, message))
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
                            addConversationListener(ConversationListener.singleListener)
                            addMessageListener(ConversationListener.singleListener)
                            addMessageListener(MessageListener.singleListener)
                            SP.put("${userName}_first_login", true)
                        }
                        cancellableContinuation.resume(Unit)
                    }

                    override fun onError(code: Int, message: String?) {
                        cancellableContinuation.resumeWithException(IMException(code, message))
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
                throw IMException(e)
            }
        } finally {
            EMClient.getInstance().chatManager().apply {
                cleanConversationsMemoryCache()
                removeConversationListener(ConversationListener.singleListener)
                removeMessageListener(ConversationListener.singleListener)
                removeMessageListener(MessageListener.singleListener)
            }
        }
    }

    fun addConversationListener(listener: ConversationListener) {
        ConversationListener.addListener(listener)
    }

    fun addMessageListener(listener: MessageListener) {
        MessageListener.addListener(listener)
    }
}