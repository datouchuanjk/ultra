package io.datou.chat.utils

import com.hyphenate.EMCallBack
import com.hyphenate.chat.EMClient
import com.hyphenate.chat.EMMessage
import io.datou.chat.listener.HANDLER
import io.datou.develop.noOpDelegate

fun EMMessage.send() {
    val message = this
    message.chatType = chatType
    message.setLocalMsgId()
    message.setMessageStatusCallback(object : EMCallBack by noOpDelegate() {
        override fun onSuccess() {
            HANDLER.onMessageSendSuccess(message)
        }

        override fun onError(code: Int, error: String) {
            HANDLER.onMessageSendFailed(message)
        }
    })
    EMClient.getInstance().chatManager().sendMessage(message)
    HANDLER.onMessageSend(message)
}