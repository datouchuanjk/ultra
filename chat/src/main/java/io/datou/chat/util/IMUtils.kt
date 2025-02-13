package io.datou.chat.util

import androidx.compose.runtime.snapshots.SnapshotStateList
import com.hyphenate.chat.EMClient
import com.hyphenate.chat.EMConversation
import com.hyphenate.chat.EMImageMessageBody
import com.hyphenate.chat.EMMessage
import com.hyphenate.chat.EMTextMessageBody
import io.datou.chat.entry.ConversationEntry
import io.datou.chat.entry.toEntry
import io.datou.develop.simpleFormat
import java.util.Calendar

/**
 * 用于ui显示的文本
 */
val EMMessage.textByShow: String
    get() = when (body) {
        is EMTextMessageBody -> {
            (body as EMTextMessageBody).message
        }

        is EMImageMessageBody -> {
            "[图片]"
        }

        else -> {
            "[未知]"
        }
    }

/**
 * 用于ui显示的状态
 */
val EMMessage.statusByShow
    get() = when (status()) {
        EMMessage.Status.CREATE -> "发送中..."
        EMMessage.Status.INPROGRESS -> "发送中..."
        EMMessage.Status.SUCCESS -> {
            if (isAcked) {
                "已读"
            } else if (isDelivered) {
                "已送达"
            } else {
                "发送成功"
            }
        }

        EMMessage.Status.FAIL -> "发送失败"
        else -> ""
    }

/**
 * 用于ui显示的时间
 */
val EMMessage.timeByShow
    get() = Calendar.getInstance().apply {
        timeInMillis = msgTime
    }.run {
        simpleFormat()
    }

/**
 * 消息是否为发送
 */
val EMMessage.isSend get() = direct() == EMMessage.Direct.SEND

/**
 * 用于ui显示的未读数
 */
val EMConversation.unReadCountByShow
    get() =
        if (unreadMsgCount > 99) "99+" else unreadMsgCount.toString()

/**
 * 消息变化->会话更新
 */
fun SnapshotStateList<ConversationEntry>.update(emMessage: EMMessage) {
    val conversationId = emMessage.conversationId()
    val entry = EMClient.getInstance().chatManager().getConversation(conversationId).toEntry()
    val index = withIndex().find { it.value.conversationId == conversationId }?.index ?: -1
    if (index < 0) {
        add(entry)
    } else {
        set(index, entry)
    }
}

/**
 * 消息不同于会话 会话的conversationId() 是唯一的
 * 消息在发送中和发送成功是不一样的 如果这个时候需要更新ui 通过msgId 去找是找不到的
 * 同理 做局部刷新key = msgId 也无法作为唯一主键 会导致新增 而不是修改
 * 所以通用扩展参数加入一个唯一标识 也就是本地msgId
 */
fun EMMessage.setLocalMsgId() {
    if (status() == EMMessage.Status.SUCCESS) {
        throw Exception("消息已发送")
    }
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

