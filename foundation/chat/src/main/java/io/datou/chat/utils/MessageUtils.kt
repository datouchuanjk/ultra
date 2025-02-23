package io.datou.chat.utils

import com.hyphenate.chat.EMClient
import com.hyphenate.chat.EMConversation
import com.hyphenate.chat.EMMessage
import io.datou.chat.listener.HANDLER

fun EMMessage.findConversation(): EMConversation? {
    return EMClient.getInstance()
        .chatManager()
        .getConversation(conversationId())
}

val EMMessage.isLastMessageInConversation: Boolean
    get() {
        return findConversation()
            ?.let { conversation ->
                conversation.lastMessage.msgId == msgId
            } ?: false
    }

fun EMMessage.setLocalMsgId() {
     setAttribute("localMsgId", msgId)
}

val EMMessage.localMsgId: String
    get() {
        return try {
            getStringAttribute("localMsgId")
        } catch (e: Exception) {
            msgId
        }
    }

val EMMessage.fromSelf get() = conversationId() == to

fun EMMessage.markReadAndAck() {
    findConversation()?.markMessageAsRead(msgId)
    EMClient.getInstance().chatManager().ackMessageRead(
        conversationId(),
        msgId
    )
    HANDLER.onMessageMarkRead(this)
}

fun EMMessage.delete() {
    findConversation()?.removeMessage(msgId)
    HANDLER.onMessageDelete(this)
}

