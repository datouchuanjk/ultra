package io.datou.chat.listener

import com.hyphenate.EMConversationListener
import com.hyphenate.EMMessageListener
import com.hyphenate.chat.EMConversation
import com.hyphenate.chat.EMMessage
import io.datou.chat.util.findConversationById

fun interface ConversationListener : EMMessageListener, EMConversationListener {
    companion object {
        internal val singleListener = ConversationListener { conversation, type ->
            _listeners.forEach {
                it.onConversationUpdate(conversation, type)
            }
        }
        private val _listeners = mutableSetOf<ConversationListener>()

        internal fun addListener(listener: ConversationListener) {
            _listeners.add(listener)
        }

        internal fun removeListener(listener: ConversationListener) {
            _listeners.remove(listener)
        }
    }

    enum class Type {
        CONVERSATION_READ_ASK,//会话已读回执
        CONVERSATION_MARK_READ,//会话已读
        MESSAGE_READ_ASK,//消息已读回执
        MESSAGE_MARK_READ,//消息已读
        MESSAGE_DELIVERED_ASK,//消息已送达回执
        MESSAGE_RECEIVED,//收到消息
        MESSAGE_SENDING,//发送消息
        MESSAGE_SEND_SUCCESS,//发送成功
        MESSAGE_SEND_FAILED,//发送失败
        NEW_CONVERSATION,//新会话
        DELETE_CONVERSATION,//删除会话
    }

    override fun onConversationUpdate() {

    }

    fun onSendMessage(message: EMMessage) {
        findConversationById(message.conversationId())?.let {
            onConversationUpdate(it, Type.MESSAGE_SENDING)
        }
    }

    fun onSendMessageSuccess(message: EMMessage) {
        findConversationById(message.conversationId())?.let {
            onConversationUpdate(it, Type.MESSAGE_SEND_SUCCESS)
        }
    }

    fun onSendMessageFailed(message: EMMessage) {
        findConversationById(message.conversationId())?.let {
            onConversationUpdate(it, Type.MESSAGE_SEND_FAILED)
        }
    }

    fun onMessageMarkRead(message: EMMessage) {
        findConversationById(message.conversationId())?.let {
            onConversationUpdate(it, Type.MESSAGE_MARK_READ)
        }
    }

    fun onConversationMarkRead(conversation: EMConversation) {
        onConversationUpdate(conversation, Type.CONVERSATION_MARK_READ)
    }

    fun onConversationDelete(conversation: EMConversation) {
        onConversationUpdate(conversation, Type.DELETE_CONVERSATION)
    }

    override fun onMessageRead(messages: MutableList<EMMessage>?) {
        messages ?: return
        messages.groupBy { it.conversationId() }
            .keys
            .mapNotNull {
                findConversationById(it)
            }.forEach {
                onConversationUpdate(it, Type.MESSAGE_READ_ASK)
            }
    }

    override fun onMessageDelivered(messages: MutableList<EMMessage>?) {
        messages ?: return
        messages.groupBy { it.conversationId() }
            .keys
            .mapNotNull {
                findConversationById(it)
            }.forEach {
                onConversationUpdate(it, Type.MESSAGE_DELIVERED_ASK)
            }
    }

    override fun onMessageReceived(messages: MutableList<EMMessage>?) {
        messages ?: return
        messages.groupBy { it.conversationId() }
            .keys
            .mapNotNull {
                findConversationById(it)
            }.forEach {
                if (it.allMsgCount == 1) {
                    onConversationUpdate(it, Type.NEW_CONVERSATION)
                } else {
                    onConversationUpdate(it, Type.MESSAGE_RECEIVED)
                }
            }
    }

    override fun onConversationRead(from: String?, to: String?) {
        from ?: return
        to ?: return
        findConversationById(from)?.let {
            onConversationUpdate(it, Type.CONVERSATION_READ_ASK)
        }
    }

    fun onConversationUpdate(conversation: EMConversation, type: Type)
}