package io.datou.chat.listener

import com.hyphenate.chat.EMConversation
import com.hyphenate.chat.EMMessage
import io.datou.chat.utils.findConversation
import io.datou.develop.findIndex
import io.datou.develop.noOpDelegate

open class ChatConversationHandlerListener(
    private val list: MutableList<EMConversation>,
    private val onFinish: () -> Unit = {}
) : ChatListener by noOpDelegate() {

    private fun MutableList<EMConversation>.handle(message: EMMessage) {
        val conversation = message.findConversation() ?: return
        handle(conversation)
    }

    private fun MutableList<EMConversation>.handle(conversation: EMConversation) {
        val index = findIndex { it.conversationId() == conversation.conversationId() }
        if (index == null) {
           add(0, conversation)
        } else {
          removeAt(index)
         add(0, conversation)
        }
    }

    override fun onMessageReceived(message: EMMessage) {
        list.handle(message)
        onFinish()
    }

    override fun onMessageSend(message: EMMessage) {
        list.handle(message)
        onFinish()
    }

    override fun onMessageSendSuccess(message: EMMessage) {
        list.handle(message)
        onFinish()
    }

    override fun onMessageSendFailed(message: EMMessage) {
        list.handle(message)
        onFinish()
    }

    override fun onMessageDeliveredAck(message: EMMessage) {
        list.handle(message)
        onFinish()
    }

    override fun onMessageReadAck(message: EMMessage) {
        list.handle(message)
        onFinish()
    }

    override fun onMessageDelete(message: EMMessage) {
        list.handle(message)
        onFinish()
    }

    override fun onMessageMarkRead(message: EMMessage) {
        list.handle(message)
        onFinish()
    }

    override fun onConversationDelete(conversation: EMConversation) {
        list.removeIf {
            it.conversationId() == conversation.conversationId()
        }
        onFinish()
    }

    override fun onConversationReadAck(conversation: EMConversation) {
        list.handle(conversation)
        onFinish()
    }

    override fun onConversationMarkRead(conversation: EMConversation) {
        list.handle(conversation)
        onFinish()
    }
}