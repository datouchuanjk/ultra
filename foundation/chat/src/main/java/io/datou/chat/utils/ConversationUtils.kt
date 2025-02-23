package io.datou.chat.utils

import com.hyphenate.EMValueCallBack
import com.hyphenate.chat.EMClient
import com.hyphenate.chat.EMConversation
import com.hyphenate.chat.EMConversation.EMConversationType
import com.hyphenate.chat.EMCursorResult
import com.hyphenate.chat.EMFetchMessageOption
import com.hyphenate.chat.EMMessage
import io.datou.chat.data.ChatResponse
import io.datou.chat.exception.ChatException
import io.datou.chat.listener.HANDLER
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

fun EMConversation.markReadAndAck() {
    markAllMessagesAsRead()
    EMClient.getInstance().chatManager().ackConversationRead(conversationId())
    HANDLER.onConversationMarkRead(this)
}

fun EMConversation.delete(isDeleteMessage: Boolean = true) {
    EMClient.getInstance().chatManager().deleteConversation(
        conversationId(),
        isDeleteMessage
    )
    HANDLER.onConversationDelete(this)
}

suspend fun EMConversation.messagesByDB(
    cursor: String? = null,
    pageSize: Int = 20
) = suspendCancellableCoroutine { continuation ->
    try {
        val list = loadMoreMsgFromDB(cursor, pageSize)
        continuation.resume(
            ChatResponse(
                data = list,
                cursor = list.firstOrNull()?.msgId,
                isNoMoreData = list.size < pageSize
            )
        )
    } catch (e: Exception) {
        continuation.resumeWithException(ChatException(e))
    }
}


suspend fun EMConversation.messagesByService(
    cursor: String? = null,
    pageSize: Int = 20
) = suspendCancellableCoroutine { cancellableContinuation ->
    val option = EMFetchMessageOption().apply {
        setIsSave(true)
    }
    EMClient.getInstance().chatManager().asyncFetchHistoryMessages(
        conversationId(),
        EMConversationType.Chat,
        pageSize,
        cursor,
        option,
        object : EMValueCallBack<EMCursorResult<EMMessage>> {
            override fun onSuccess(result: EMCursorResult<EMMessage>?) {
                cancellableContinuation.resume(
                    ChatResponse(
                        data = result?.data?.filterNotNull() ?: emptyList(),
                        cursor = result?.cursor,
                        isNoMoreData = (result?.data?.size ?: 0) < pageSize
                    )
                )
            }

            override fun onError(code: Int, message: String?) {
                cancellableContinuation.resumeWithException(
                    ChatException(
                        code,
                        message
                    )
                )
            }
        }
    )
}