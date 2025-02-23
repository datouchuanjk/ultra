package io.datou.chat.listener

import com.hyphenate.EMConversationListener
import com.hyphenate.EMMessageListener
import com.hyphenate.chat.EMClient
import com.hyphenate.chat.EMConversation
import com.hyphenate.chat.EMMessage

interface ChatListener : EMMessageListener, EMConversationListener {
    override fun onConversationUpdate() {}
    override fun onConversationRead(from: String?, to: String?) {
        from ?: return
        to ?: return
        EMClient.getInstance().chatManager().getConversation(from)?.let {
            onConversationReadAck(it)
        }
    }

    override fun onMessageRead(messages: MutableList<EMMessage>?) {
        messages ?: return
        messages.forEach {
            onMessageReadAck(it)
        }
    }

    override fun onMessageDelivered(messages: MutableList<EMMessage>?) {
        messages ?: return
        messages.forEach {
            onMessageDeliveredAck(it)
        }
    }

    override fun onMessageReceived(messages: MutableList<EMMessage>?) {
        messages ?: return
        messages.forEach {
            onMessageReceived(it)
        }
    }

    fun onUnreadMessageCount(count: Int)
    fun onMessageReceived(message: EMMessage)
    fun onMessageSend(message: EMMessage)
    fun onMessageSendSuccess(message: EMMessage)
    fun onMessageSendFailed(message: EMMessage)
    fun onMessageDeliveredAck(message: EMMessage)
    fun onMessageReadAck(message: EMMessage)
    fun onMessageDelete(message: EMMessage)
    fun onMessageMarkRead(message: EMMessage)
    fun onConversationDelete(conversation: EMConversation)
    fun onConversationReadAck(conversation: EMConversation)
    fun onConversationMarkRead(conversation: EMConversation)
}