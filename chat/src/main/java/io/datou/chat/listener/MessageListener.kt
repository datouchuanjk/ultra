package io.datou.chat.listener

import com.hyphenate.EMMessageListener
import com.hyphenate.chat.EMMessage

/**
 * 消息监听
 */
fun interface MessageListener : EMMessageListener {
    companion object {
        internal val singleListener = MessageListener { messages, type ->
            _listeners.forEach {
                it.onMessageUpdate(messages, type)
            }
        }
        private val _listeners = mutableSetOf<MessageListener>()

        internal fun addListener(listener: MessageListener) {
            _listeners.add(listener)
        }

        internal fun removeListener(listener: MessageListener) {
            _listeners.remove(listener)
        }
    }

    enum class Type {
        MESSAGE_RECEIVED, //收到消息
        MESSAGE_SENDING,//发送消息
        MESSAGE_SEND_SUCCESS,//发送成功
        MESSAGE_SEND_FAILED,//发送失败
        READ_ASK,// 已读回执
        DELIVERED_ASK,//已送达回执
        MARK_READ//已读
    }

    fun onSendMessage(message: EMMessage) {
        onMessageUpdate(message, Type.MESSAGE_SENDING)
    }

    fun onSendMessageSuccess(message: EMMessage) {
        onMessageUpdate(message, Type.MESSAGE_SEND_SUCCESS)
    }

    fun onSendMessageFailed(message: EMMessage) {
        onMessageUpdate(message, Type.MESSAGE_SEND_FAILED)
    }

    fun onMessageMarkRead(message: EMMessage) {
        onMessageUpdate(message, Type.MARK_READ)
    }

    override fun onMessageRead(messages: MutableList<EMMessage>?) {
        messages ?: return
        messages.forEach { message ->
            onMessageUpdate(message, Type.READ_ASK)
        }
    }

    override fun onMessageDelivered(messages: MutableList<EMMessage>?) {
        messages ?: return
        messages.forEach { message ->
            onMessageUpdate(message, Type.DELIVERED_ASK)
        }
    }

    override fun onMessageReceived(messages: MutableList<EMMessage>?) {
        messages ?: return
        messages.forEach { message ->
            onMessageUpdate(message, Type.MESSAGE_RECEIVED)
        }
    }

    fun onMessageUpdate(messages: EMMessage, type: Type)
}