package io.datou.chat.listener

import com.hyphenate.chat.EMClient
import com.hyphenate.chat.EMConversation
import com.hyphenate.chat.EMMessage

internal val INSTANCE by lazy {
    DefaultChatListener()
}

internal class DefaultChatListener : ChatListener() {
    private val _listeners = mutableSetOf<ChatListener>()
    internal fun add(listener: ChatListener) {
        _listeners.add(listener)
    }

    internal fun remove(listener: ChatListener) {
        _listeners.remove(listener)
    }

    private fun dispatch(block: (ChatListener) -> Unit) {
        _listeners.forEach(block)
    }

    internal fun refreshUnreadMessageCount() {
        unreadMessageCount(
            EMClient.getInstance().chatManager().unreadMessageCount
        )
    }

    override fun onMessageReceived(message: EMMessage) {
        refreshUnreadMessageCount()
        dispatch { it.onMessageReceived(message) }
    }

    override fun unreadMessageCount(count: Int) {
        dispatch { it.unreadMessageCount(count) }
    }

    override fun onMessageSend(message: EMMessage) {
        dispatch { it.onMessageSend(message) }
    }

    override fun onMessageSendSuccess(message: EMMessage) {
        dispatch { it.onMessageSendSuccess(message) }
    }

    override fun onMessageSendFailed(message: EMMessage) {
        dispatch { it.onMessageSendFailed(message) }
    }

    override fun onMessageDeliveredAck(message: EMMessage) {
        dispatch { it.onMessageDeliveredAck(message) }
    }

    override fun onMessageReadAck(message: EMMessage) {
        dispatch { it.onMessageReadAck(message) }
    }

    override fun onMessageDelete(message: EMMessage) {
        dispatch { it.onMessageDelete(message) }
        refreshUnreadMessageCount()
    }

    override fun onMessageMarkRead(message: EMMessage) {
        dispatch { it.onMessageMarkRead(message) }
        refreshUnreadMessageCount()
    }

    override fun onConversationDelete(conversation: EMConversation) {
        dispatch { it.onConversationDelete(conversation) }
        refreshUnreadMessageCount()
    }

    override fun onConversationReadAck(conversation: EMConversation) {
        dispatch { it.onConversationReadAck(conversation) }
    }

    override fun onConversationMarkRead(conversation: EMConversation) {
        dispatch { it.onConversationMarkRead(conversation) }
        refreshUnreadMessageCount()
    }
}