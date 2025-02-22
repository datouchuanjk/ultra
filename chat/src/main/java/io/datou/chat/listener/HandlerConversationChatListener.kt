package io.datou.chat.listener

import android.util.Printer
import com.hyphenate.chat.EMConversation
import com.hyphenate.chat.EMMessage
import io.datou.chat.utils.findConversation
import io.datou.develop.findIndex

open class HandlerConversationChatListener(
    private val list: MutableList<EMConversation>,
    private val onFinish:()->Unit ={}
) : ChatListener() {

    override fun onMessageReceived(message: EMMessage) {
        val conversation = message.findConversation() ?: return
        val index = list.findIndex { it.conversationId() == conversation.conversationId() }
        if (index == null) {
            list.add(0, conversation)
        } else {
            list.removeAt(index)
            list.add(0, conversation)
        }
        onFinish()
    }

    override fun unreadMessageCount(count: Int) {
    }

    override fun onMessageSend(message: EMMessage) {
        val conversation = message.findConversation() ?: return
        val index = list.findIndex { it.conversationId() == conversation.conversationId() }
        if (index == null) {
            list.add(0, conversation)
        } else {
            list.removeAt(index)
            list.add(0, conversation)
        }
        onFinish()
    }

    override fun onMessageSendSuccess(message: EMMessage) {
        val conversation = message.findConversation() ?: return
        val index = list.findIndex { it.conversationId() == conversation.conversationId() }
        if (index == null) {
            list.add(0, conversation)
        } else {
            list.removeAt(index)
            list.add(0, conversation)
        }
        onFinish()
    }

    override fun onMessageSendFailed(message: EMMessage) {
        val conversation = message.findConversation() ?: return
        val index = list.findIndex { it.conversationId() == conversation.conversationId() }
        if (index == null) {
            list.add(0, conversation)
        } else {
            list.removeAt(index)
            list.add(0, conversation)
        }
        onFinish()
    }

    override fun onMessageDeliveredAck(message: EMMessage) {
        val conversation = message.findConversation() ?: return
        val index = list.findIndex { it.conversationId() == conversation.conversationId() }
        if (index == null) {
            list.add(0, conversation)
        } else {
            list.removeAt(index)
            list.add(0, conversation)
        }
        onFinish()
    }

    override fun onMessageReadAck(message: EMMessage) {
        val conversation = message.findConversation() ?: return
        val index = list.findIndex { it.conversationId() == conversation.conversationId() }
        if (index == null) {
            list.add(0, conversation)
        } else {
            list.removeAt(index)
            list.add(0, conversation)
        }
        onFinish()
    }

    override fun onMessageDelete(message: EMMessage) {
        val conversation = message.findConversation() ?: return
        val index = list.findIndex { it.conversationId() == conversation.conversationId() }
        if (index == null) {
            list.add(0, conversation)
        } else {
            list.removeAt(index)
            list.add(0, conversation)
        }
        onFinish()
    }

    override fun onMessageMarkRead(message: EMMessage) {
        val conversation = message.findConversation() ?: return
        val index = list.findIndex { it.conversationId() == conversation.conversationId() }
        if (index == null) {
            list.add(0, conversation)
        } else {
            list.removeAt(index)
            list.add(0, conversation)
        }
        onFinish()
    }

    override fun onConversationDelete(conversation: EMConversation) {
        list.removeIf {
            it.conversationId() == conversation.conversationId()
        }
        onFinish()
    }

    override fun onConversationReadAck(conversation: EMConversation) {
        val index = list.findIndex { it.conversationId() == conversation.conversationId() }
        if (index == null) {
            list.add(0, conversation)
        } else {
            list.removeAt(index)
            list.add(0, conversation)
        }
        onFinish()
    }

    override fun onConversationMarkRead(conversation: EMConversation) {
        val index = list.findIndex { it.conversationId() == conversation.conversationId() }
        if (index == null) {
            list.add(0, conversation)
        } else {
            list.removeAt(index)
            list.add(0, conversation)
        }
        onFinish()
    }
}