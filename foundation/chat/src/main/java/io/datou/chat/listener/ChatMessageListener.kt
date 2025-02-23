package io.datou.chat.listener

import com.hyphenate.chat.EMMessage
import io.datou.chat.utils.localMsgId
import io.datou.develop.noOpDelegate
import io.datou.develop.reinsertIf

open class ChatMessageListener(
    private val list: MutableList<EMMessage>,
    private val onFinish: () -> Unit = {}
) : ChatListener by noOpDelegate() {

    override fun onMessageReceived(message: EMMessage) {
        list.add(0, message)
        onFinish()
    }

    override fun onMessageSend(message: EMMessage) {
        list.add(0, message)
        onFinish()
    }

    override fun onMessageSendSuccess(message: EMMessage) {
        list.reinsertIf(message) {
            it.localMsgId == message.localMsgId
        }
        onFinish()
    }

    override fun onMessageSendFailed(message: EMMessage) {
        list.reinsertIf(message) {
            it.localMsgId == message.localMsgId
        }
        onFinish()
    }

    override fun onMessageDeliveredAck(message: EMMessage) {
        list.reinsertIf(message) {
            it.msgId == message.msgId
        }
        onFinish()
    }

    override fun onMessageReadAck(message: EMMessage) {
        list.reinsertIf(message) {
            it.msgId == message.msgId
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
        list.reinsertIf(message) {
            it.msgId == message.msgId
        }
        onFinish()
    }
}