package io.datou.chat.listener

import com.hyphenate.EMConversationListener
import com.hyphenate.EMMessageListener
import com.hyphenate.chat.EMClient
import com.hyphenate.chat.EMConversation
import com.hyphenate.chat.EMMessage
import io.datou.chat.utils.findConversation

abstract class ChatListener : EMMessageListener, EMConversationListener {
    final override fun onConversationUpdate() {}
    final override fun onConversationRead(from: String?, to: String?) {
        from ?: return
        to ?: return
        EMClient.getInstance().chatManager().getConversation(from)?.let {
            onConversationReadAck(it)
        }
    }

    final override fun onMessageRead(messages: MutableList<EMMessage>?) {
        messages ?: return
        messages.forEach {
            onMessageReadAck(it)
        }
    }

    final override fun onMessageDelivered(messages: MutableList<EMMessage>?) {
        messages ?: return
        messages.forEach {
            onMessageDeliveredAck(it)
        }
    }

    final override fun onMessageReceived(messages: MutableList<EMMessage>?) {
        messages ?: return
        messages.forEach {
            onMessageReceived(it)
        }
    }

    abstract fun unreadMessageCount(count: Int)
    abstract fun onMessageReceived(message: EMMessage)
    abstract fun onMessageSend(message: EMMessage)
    abstract fun onMessageSendSuccess(message: EMMessage)
    abstract fun onMessageSendFailed(message: EMMessage)
    abstract fun onMessageDeliveredAck(message: EMMessage)
    abstract fun onMessageReadAck(message: EMMessage)
    abstract fun onMessageDelete(message: EMMessage)
    abstract fun onMessageMarkRead(message: EMMessage)
    abstract fun onConversationDelete(conversation: EMConversation)
    abstract fun onConversationReadAck(conversation: EMConversation)
    abstract fun onConversationMarkRead(conversation: EMConversation)
}