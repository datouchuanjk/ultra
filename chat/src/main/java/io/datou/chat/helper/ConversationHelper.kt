package io.datou.chat.helper

import com.hyphenate.EMValueCallBack
import com.hyphenate.chat.EMClient
import com.hyphenate.chat.EMConversation
import io.datou.chat.exception.IMException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * 会话帮助类
 */
object ConversationHelper {

    /**
     * 获取会话总未读数
     */
    fun getUnreadMessageCount(): Int {
        return EMClient.getInstance().chatManager().unreadMessageCount
    }

    /**
     * 同步获取会话
     */
    suspend fun getConversationsFromDB(filter: (String) -> Boolean = { true }) =
        suspendCoroutine { continuation ->
            EMClient.getInstance().chatManager()
                .asyncFilterConversationsFromDB({
                    filter(it.conversationId())
                }, false,
                    object : EMValueCallBack<List<EMConversation?>?> {
                        override fun onSuccess(filterConversations: List<EMConversation?>?) {
                            val list = filterConversations
                                ?.filterNotNull()
                                ?: emptyList()
                            continuation.resume(list)
                        }

                        override fun onError(error: Int, errorMsg: String) {
                            continuation.resumeWithException(IMException(error, errorMsg))
                        }
                    })
        }
}
