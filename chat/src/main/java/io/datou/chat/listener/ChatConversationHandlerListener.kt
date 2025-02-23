package io.datou.chat.listener

import com.hyphenate.EMMessageListener
import com.hyphenate.chat.EMConversation
import com.hyphenate.chat.EMMessage
import io.datou.chat.utils.findConversation
import io.datou.develop.findIndex
import io.datou.develop.noOpDelegate

open class ChatConversationHandlerListener(
    private val list: MutableList<EMConversation>,
    private val onFinish: () -> Unit = {}
) : ChatListener by noOpDelegate() {

    private fun MutableList<EMConversation>.handleByMessage(message: EMMessage) {
        val conversation = message.findConversation() ?: return
        handleByMessage(conversation)
    }

    private fun MutableList<EMConversation>.handleByMessage(conversation: EMConversation) {
        val index = findIndex { it.conversationId() == conversation.conversationId() }
        if (index == null) {
           add(0, conversation)
        } else {
          removeAt(index)
         add(0, conversation)
        }
    }

    override fun onMessageReceived(message: EMMessage) {
        list.handleByMessage(message)
        onFinish()
    }

    override fun onMessageSend(message: EMMessage) {
        list.handleByMessage(message)
        onFinish()
    }

    override fun onMessageSendSuccess(message: EMMessage) {
        list.handleByMessage(message)
        onFinish()
    }

    override fun onMessageSendFailed(message: EMMessage) {
        list.handleByMessage(message)
        onFinish()
    }

    override fun onMessageDeliveredAck(message: EMMessage) {
        list.handleByMessage(message)
        onFinish()
    }

    override fun onMessageReadAck(message: EMMessage) {
        list.handleByMessage(message)
        onFinish()
    }

    override fun onMessageDelete(message: EMMessage) {
        list.handleByMessage(message)
        onFinish()
    }

    override fun onMessageMarkRead(message: EMMessage) {
        list.handleByMessage(message)
        onFinish()
    }

    override fun onConversationDelete(conversation: EMConversation) {
        list.removeIf {
            it.conversationId() == conversation.conversationId()
        }
        onFinish()
    }

    override fun onConversationReadAck(conversation: EMConversation) {
        list.handleByMessage(conversation)
        onFinish()
    }

    override fun onConversationMarkRead(conversation: EMConversation) {
        list.handleByMessage(conversation)
        onFinish()
    }
}