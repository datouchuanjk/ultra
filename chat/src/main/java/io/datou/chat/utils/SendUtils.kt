package io.datou.chat.utils

import com.hyphenate.EMCallBack
import com.hyphenate.chat.EMClient
import com.hyphenate.chat.EMMessage
import io.datou.chat.listener.INSTANCE
import io.datou.develop.noOpDelegate

fun EMMessage.send() {
    val message = this
    message.chatType = chatType
    message.setLocalMsgId()
    message.setMessageStatusCallback(object : EMCallBack by noOpDelegate() {
        override fun onSuccess() {
            INSTANCE.onMessageSendSuccess(message)
        }

        override fun onError(code: Int, error: String) {
            INSTANCE.onMessageSendFailed(message)
        }
    })
    EMClient.getInstance().chatManager().sendMessage(message)
    INSTANCE.onMessageSend(message)
}