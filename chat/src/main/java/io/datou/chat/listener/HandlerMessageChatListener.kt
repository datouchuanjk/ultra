package io.datou.chat.listener

import com.hyphenate.chat.EMConversation
import com.hyphenate.chat.EMMessage
import io.datou.chat.utils.findConversation
import io.datou.chat.utils.localMsgId
import io.datou.develop.findIndex
import io.datou.develop.removeIf

open class HandlerMessageChatListener(
    private val list: MutableList<EMMessage>,
    private val onFinish:()->Unit ={}
) : ChatListener() {

    override fun onMessageReceived(message: EMMessage) {
        list.add(0, message)
        onFinish()
    }

    override fun unreadMessageCount(count: Int) {
    }

    override fun onMessageSend(message: EMMessage) {
        list.add(0, message)
        onFinish()
    }

    override fun onMessageSendSuccess(message: EMMessage) {
        list.findIndex {
            it.localMsgId == message.localMsgId
        }?.let {
            list.removeAt(it)
            list.add(it, message)
        }
        onFinish()
    }

    override fun onMessageSendFailed(message: EMMessage) {
        list.findIndex {
            it.localMsgId == message.localMsgId
        }?.let {
            list.removeAt(it)
            list.add(it, message)
        }
        onFinish()
    }

    override fun onMessageDeliveredAck(message: EMMessage) {
        list.findIndex {
            it.msgId == message.msgId
        }?.let {
            list.removeAt(it)
            list.add(it, message)
        }
        onFinish()
    }

    override fun onMessageReadAck(message: EMMessage) {
        list.findIndex {
            it.msgId == message.msgId
        }?.let {
            list.removeAt(it)
            list.add(it, message)
        }
        onFinish()
    }

    override fun onMessageDelete(message: EMMessage) {
        list.removeIf {
            message.msgId == it.msgId
        }
        onFinish()
    }

    override fun onMessageMarkRead(message: EMMessage) {
        list.findIndex {
            it.msgId == message.msgId
        }?.let {
            list.removeAt(it)
            list.add(it, message)
        }
        onFinish()
    }


    override fun onConversationDelete(conversation: EMConversation) {

    }

    override fun onConversationReadAck(conversation: EMConversation) {

    }

    override fun onConversationMarkRead(conversation: EMConversation) {

    }
}