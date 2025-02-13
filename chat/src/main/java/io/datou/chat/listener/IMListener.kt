package io.datou.chat.listener

import com.hyphenate.EMMessageListener
import com.hyphenate.chat.EMMessage



/**
 * 通用的监听
 */
fun interface IMListener : EMMessageListener {
    /**
     * 收到消息
     */
    override fun onMessageReceived(messages: MutableList<EMMessage>?) {
        messages?.toList()?.forEach {
            onMessage(it)
        }
    }

    /**
     * 消息状态变更
     */
    override fun onMessageChanged(message: EMMessage?, change: Any?) {
        message?.let {
            onMessage(it)
        }
    }

    /**
     * 发送消息
     */
    fun onSendMessage(message: EMMessage) {
        onMessage(message)
    }

    /**
     * 发送消息成功
     */
    fun onSendMessageSuccess(message: EMMessage) {
        onMessage(message)
    }

    /**
     * 发送消息失败
     */
    fun onSendMessageError(message: EMMessage) {
        onMessage(message)
    }

    fun onMessage(message: EMMessage)
}