package io.datou.chat.helper

import android.app.Application
import com.hyphenate.EMCallBack
import com.hyphenate.EMConnectionListener
import com.hyphenate.chat.EMClient
import com.hyphenate.chat.EMMessage
import com.hyphenate.chat.EMOptions
import io.datou.chat.exception.IMException
import io.datou.chat.listener.IMConnectionListener
import io.datou.chat.listener.IMListener
import io.datou.chat.util.IMLog
import io.datou.chat.util.setLocalMsgId
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


/**
 * IM
 * 处理基本的初始化
 * 登录
 * 登出
 */
object IMHelper {

    /**
     * 连接监听
     */
    private val _connectionListener = IMConnectionListener()

    /**
     * 监听
     */
    private val _listener = IMListener { message ->
        _listeners.forEach { listener ->
            listener.onMessage(message)
        }
    }

    /**
     * 初始化
     */
    fun init(application: Application, key: String) {
        val emOptions = EMOptions().apply {
            appKey = key
            autoLogin = true
            isIncludeSendMessageInMessageListener = false
        }
        EMClient.getInstance().init(application, emOptions)
    }

    /**
     * 同步token登录
     */
    suspend fun loginByToken(
        userName: String,
        token: String,
    ) {
        logout()
        suspendCoroutine { continuation ->
            EMClient.getInstance().loginWithToken(
                userName,
                token,
                object : EMCallBack {
                    override fun onSuccess() {
                        EMClient.getInstance().addConnectionListener(_connectionListener)
                        EMClient.getInstance().chatManager().addMessageListener(_listener)
                        EMClient.getInstance().chatManager().loadAllConversations()
                        continuation.resume(Unit)
                    }

                    override fun onError(code: Int, message: String?) {
                        continuation.resumeWithException(IMException(code, message))
                    }
                }
            )
        }
    }

    /**
     * 同步密码登录
     */
    suspend fun loginByPwd(
        userName: String,
        pwd: String,
    ) {
        logout()
        suspendCoroutine { continuation ->
            EMClient.getInstance().login(
                userName,
                pwd,
                object : EMCallBack {
                    override fun onSuccess() {
                        EMClient.getInstance().addConnectionListener(_connectionListener)
                        EMClient.getInstance().chatManager().addMessageListener(_listener)
                        EMClient.getInstance().chatManager().loadAllConversations()
                        continuation.resume(Unit)
                    }

                    override fun onError(code: Int, message: String?) {
                        continuation.resumeWithException(IMException(code, message))
                    }
                }
            )
        }
    }

    /**
     * 同步退出登录
     */
    private suspend fun logout() {
        suspendCoroutine { continuation ->
            try {
                EMClient.getInstance().logout(true)
            } catch (e: Exception) {
                try {
                    EMClient.getInstance().logout(false)
                } catch (e: Exception) {
                    continuation.resumeWithException(IMException(-1, e.message))
                }
            } finally {
                EMClient.getInstance().removeConnectionListener(_connectionListener)
                EMClient.getInstance().chatManager().removeMessageListener(_listener)
                EMClient.getInstance().chatManager().cleanConversationsMemoryCache()
                continuation.resume(Unit)
            }
        }
    }

    /**
     * 监听管理
     */
    private val _listeners = mutableListOf<IMListener>()

    /**
     * 添加监听
     */
    fun addListener(listener: IMListener) {
        _listeners.add(listener)
    }

    /**
     * 移除监听
     */
    fun removeListener(listener: IMListener) {
        _listeners.remove(listener)
    }

    /**
     * 处理监听
     */
    internal fun handleListener(block: (IMListener) -> Unit) {
        _listeners.forEach(block)
    }

    /**
     * 移位置到MessageHelper
     */
    fun sendMessage(emMessage: EMMessage) {
        emMessage.setLocalMsgId()
        emMessage.setMessageStatusCallback(object : EMCallBack {
            override fun onSuccess() {
                handleListener {
                    it.onSendMessageSuccess(emMessage)
                }
            }

            override fun onError(p0: Int, p1: String?) {
                handleListener {
                    it.onSendMessageError(emMessage)
                }
            }

        })
        EMClient.getInstance().chatManager().sendMessage(emMessage)
        handleListener {
            it.onSendMessage(emMessage)
        }
    }
}