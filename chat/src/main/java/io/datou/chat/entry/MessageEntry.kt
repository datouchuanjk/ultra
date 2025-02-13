package io.datou.chat.entry

import com.hyphenate.chat.EMMessage
import com.hyphenate.chat.EMTextMessageBody
import io.datou.chat.util.isSend
import io.datou.chat.util.localMsgId
import io.datou.chat.util.statusByShow
import io.datou.chat.util.timeByShow


/**
 * 消息对象
 */
abstract class MessageEntry {
    lateinit var localMsgId: String
    lateinit var msgId: String
    lateinit var status: String
    lateinit var time: String
    var isSend: Boolean = false
}

/**
 * 文本消息对象
 */
data class TxtMessageEntry(
    val text: String
) : MessageEntry()

/**
 * 环信消息对象转[MessageEntry]
 */
fun EMMessage.toEntry() = when (body) {
    is EMTextMessageBody -> {
        TxtMessageEntry(
            text = (body as EMTextMessageBody).message
        )
    }

    else -> error("")
}.let {
    it.localMsgId = localMsgId
    it.msgId = msgId
    it.status = statusByShow
    it.time = timeByShow
    it.isSend = isSend
}
