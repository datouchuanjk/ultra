package io.datou.chat.helper

import androidx.compose.runtime.mutableStateListOf
import com.hyphenate.EMCallBack
import com.hyphenate.EMMessageListener
import com.hyphenate.chat.EMClient
import com.hyphenate.chat.EMMessage
import io.datou.chat.entry.MessageEntry
import io.datou.chat.entry.toEntry
import io.datou.chat.util.IMLog
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * 消息
 */
class MessageHelper(val conversationId: String) {
    /**
     * 消息列表
     */
    private val _messages = mutableStateListOf<MessageEntry>()
    val messages: List<MessageEntry> get() = _messages

    /**
     * 更新消息
     * 如果有就更新 否则就新增到第一个索引位
     */
    fun updateMessage(messages: List<EMMessage?>?) {
        messages?.filterNotNull()?.forEach { message ->
            val index = _messages
                .withIndex()
                .find {
                    it.value.msgId == message.msgId
                }?.index ?: -1
            if (index >= 0) {
                _messages[index] = message.toEntry()
            } else {
                _messages.add(0, message.toEntry())
            }
        }
    }


    /**
     * 消息监听
     */
    internal val messageListener = object : EMMessageListener {
        /**
         * 收到消息
         */
        override fun onMessageReceived(messages: MutableList<EMMessage>?) {
            IMLog.e("onMessageReceived messages:$messages")
            updateMessage(messages)
        }

        /**
         * 收到已读回执
         */
        override fun onMessageRead(messages: MutableList<EMMessage>?) {
            messages ?: return
            IMLog.e("onMessageRead messages:$messages")
            updateMessage(messages)
        }

        /**
         * 消息状态变更
         */
        override fun onMessageChanged(message: EMMessage?, change: Any?) {
            IMLog.e("onMessageChanged message:$message change:$change")
            updateMessage(listOf(message))
        }
    }

    /**
     * 绑定
     * 注册消息监听
     */
    internal fun bind() {
        EMClient.getInstance().chatManager().addMessageListener(messageListener)
    }

    /**
     * 解绑
     * 移除消息监听
     */
    internal fun unbind() {
        EMClient.getInstance().chatManager().removeMessageListener(messageListener)
    }

    /**
     * 同步获取消息
     */
    suspend fun getMessagesFromDB(conversationId: String, id: String? = null) {
        suspendCoroutine { continuation ->
            val conversation = EMClient.getInstance().chatManager().getConversation(conversationId)
            if (id == null) {
                _messages.clear()
                _messages.addAll(conversation.allMessages.map { it.toEntry() })
            }
            conversation.loadMoreMsgFromDB(_messages.getOrNull(0)?.msgId, 100).map {
                it.toEntry()
            }.let {
                _messages.addAll(it)
            }
            continuation.resume(Unit)
        }
    }

    /**
     * 发送消息
     */
    private fun sendChatMessage(message: EMMessage) {
        message.chatType = EMMessage.ChatType.Chat
        message.setMessageStatusCallback(object : EMCallBack {
            override fun onSuccess() {
                IMLog.e("setMessageStatusCallback onSuccess")
                updateMessage(listOf(message))
            }

            override fun onError(code: Int, error: String) {
                IMLog.e("setMessageStatusCallback onError code:$code error:$error")
                updateMessage(listOf(message))
            }

            override fun onProgress(progress: Int, status: String) {
            }
        })
        EMClient.getInstance().chatManager().sendMessage(message)
        updateMessage(listOf(message))
    }

    /**
     * 设置会话已读
     * 发送已读回执
     * 更新会话
     * 更新未读数
     */
    fun markAllMessagesAsRead(conversationId: String) {
        try {
            //设置会话已读
            val conversation = EMClient.getInstance().chatManager().getConversation(conversationId)
            conversation?.markAllMessagesAsRead()
            EMClient.getInstance().chatManager().ackConversationRead(conversationId)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 删除会话
     * 更新会话
     * 更新未读数
     */
    fun deleteConversation(conversationId: String) {
        EMClient.getInstance().chatManager().deleteConversation(conversationId, true)
    }
}

