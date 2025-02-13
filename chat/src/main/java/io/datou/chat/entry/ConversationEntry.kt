package io.datou.chat.entry

import com.hyphenate.chat.EMConversation
import io.datou.chat.util.isSend
import io.datou.chat.util.statusByShow
import io.datou.chat.util.textByShow
import io.datou.chat.util.timeByShow
import io.datou.chat.util.unReadCountByShow

/**
 * 会话对象
 */
data class ConversationEntry(
    val conversationId: String,
    val text: String,
    val status: String,
    val time: String,
    val unReadCount: String,
    val isSend: Boolean,
)

/**
 * 环信会话对象转[ConversationEntry]
 */
fun EMConversation.toEntry() = ConversationEntry(
    conversationId = conversationId(),
    text = lastMessage.textByShow,
    status = lastMessage.statusByShow,
    time = lastMessage.timeByShow,
    unReadCount = unReadCountByShow,
    isSend = lastMessage.isSend,
)


