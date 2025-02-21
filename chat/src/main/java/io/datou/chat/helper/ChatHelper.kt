package io.datou.chat.helper

import com.hyphenate.EMCallBack
import com.hyphenate.EMValueCallBack
import com.hyphenate.chat.EMClient
import com.hyphenate.chat.EMConversation.EMConversationType
import com.hyphenate.chat.EMCursorResult
import com.hyphenate.chat.EMFetchMessageOption
import com.hyphenate.chat.EMMessage
import io.datou.chat.listener.ConversationListener
import io.datou.chat.listener.MessageListener
import io.datou.chat.util.IMException
import io.datou.chat.util.isSendBySelf
import io.datou.chat.util.setLocalMsgId
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException


class ChatHelper(val conversationId: String) {

    private val _conversation = EMClient.getInstance().chatManager().getConversation(conversationId)
    private val _pageSize = 20
    private var _cursor: String? = null
    private var _startMsgId: String? = null
    private var _isNoMore: Boolean = false
    val isNoMore get() = _isNoMore

    suspend fun refresh(): List<EMMessage> {
        _isNoMore = false
        return if (IMHelper.isFirstLogin()) {
            fromService()
        } else {
            fromDB()
        }
    }

    suspend fun loadMore(): List<EMMessage> {
        if (_isNoMore) {
            return emptyList()
        }
        return refresh()
    }

    private suspend fun fromDB(): List<EMMessage> {
        return suspendCancellableCoroutine { continuation ->
            try {
                val list = _conversation.loadMoreMsgFromDB(_startMsgId, _pageSize)
                _isNoMore = list.size < _pageSize
                _startMsgId = list.firstOrNull()?.msgId
                continuation.resume(list)
            } catch (e: Exception) {
                continuation.resumeWithException(IMException(e))
            }
        }
    }

    private suspend fun fromService(): List<EMMessage> {
        return suspendCancellableCoroutine { cancellableContinuation ->
            val option = EMFetchMessageOption().apply {
                setIsSave(true)
            }
            EMClient.getInstance().chatManager().asyncFetchHistoryMessages(
                conversationId,
                EMConversationType.Chat,
                _pageSize,
                _cursor,
                option,
                object : EMValueCallBack<EMCursorResult<EMMessage>> {
                    override fun onSuccess(result: EMCursorResult<EMMessage>?) {
                        _cursor = result?.cursor
                        _isNoMore = (result?.data?.size ?: 0) < _pageSize
                        val list = result?.data
                            ?.filterNotNull()
                            ?: emptyList()
                        cancellableContinuation.resume(list)
                    }

                    override fun onError(code: Int, message: String?) {
                        cancellableContinuation.resumeWithException(
                            IMException(
                                code,
                                message
                            )
                        )
                    }
                }
            )
        }
    }

    fun sendChatMessage(message: EMMessage) {
        message.chatType = EMMessage.ChatType.Chat
        message.setLocalMsgId()
        message.setMessageStatusCallback(object : EMCallBack {
            override fun onSuccess() {
                ConversationListener.singleListener.onSendMessageSuccess(message)
                MessageListener.singleListener.onSendMessageSuccess(message)
            }

            override fun onError(code: Int, error: String) {
                ConversationListener.singleListener.onSendMessageFailed(message)
                MessageListener.singleListener.onSendMessageFailed(message)
            }

            override fun onProgress(progress: Int, status: String) {
            }
        })
        try {
            EMClient.getInstance().chatManager().sendMessage(message)
            ConversationListener.singleListener.onSendMessage(message)
            MessageListener.singleListener.onSendMessage(message)
        } catch (e: Exception) {
            ConversationListener.singleListener.onSendMessageFailed(message)
            MessageListener.singleListener.onSendMessageFailed(message)
        }
    }


    fun markAllMessagesAsRead() {
        try {
            _conversation.markAllMessagesAsRead()
            EMClient.getInstance().chatManager().ackConversationRead(conversationId)
            ConversationListener.singleListener.onConversationMarkRead(_conversation)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun markMessagesAsRead(message: EMMessage) {
        if (_conversation.conversationId() != message.conversationId()) {
            return
        }
        try {
            _conversation.markMessageAsRead(message.msgId)
            EMClient.getInstance().chatManager()
                .ackMessageRead(_conversation.conversationId(), message.msgId)
            ConversationListener.singleListener.onMessageMarkRead(message)
            MessageListener.singleListener.onMessageMarkRead(message)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun deleteConversations(isDeleteMessage: Boolean = true) {
        EMClient.getInstance().chatManager()
            .deleteConversation(_conversation.conversationId(), isDeleteMessage)
        ConversationListener.singleListener.onConversationDelete(_conversation)
    }
}

