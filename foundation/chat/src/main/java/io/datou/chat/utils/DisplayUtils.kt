package io.datou.chat.utils

import com.hyphenate.chat.EMConversation
import com.hyphenate.chat.EMCustomMessageBody
import com.hyphenate.chat.EMFileMessageBody
import com.hyphenate.chat.EMImageMessageBody
import com.hyphenate.chat.EMLocationMessageBody
import com.hyphenate.chat.EMMessage
import com.hyphenate.chat.EMTextMessageBody
import com.hyphenate.chat.EMVideoMessageBody
import com.hyphenate.chat.EMVoiceMessageBody
import io.datou.develop.simpleFormat
import java.util.Calendar

val EMMessage.displayStatus
    get() = when (status()) {
        EMMessage.Status.CREATE -> 0
        EMMessage.Status.INPROGRESS -> 0
        EMMessage.Status.SUCCESS -> {
            if (isAcked) {
                3
            } else if (isDelivered) {
                2
            } else {
                1
            }
        }

        EMMessage.Status.FAIL -> -1
        else -> 0
    }

val EMMessage.displayText: String
    get() = when (val currentBody = body) {
        is EMTextMessageBody -> currentBody.message
        is EMImageMessageBody -> "[图片]"
        is EMVideoMessageBody -> "[视频]"
        is EMVoiceMessageBody -> "[语音]"
        is EMFileMessageBody -> "[文件]"
        is EMLocationMessageBody -> "[位置]"
        is EMCustomMessageBody -> currentBody.event()
        else -> "[未知]"
    }

val EMMessage.displayTime: String
    get() = Calendar.getInstance().apply {
        timeInMillis = msgTime
    }.run {
        simpleFormat()
    }
val EMConversation.displayUnreadMsgCount
    get() = if (unreadMsgCount > 99) "99+" else unreadMsgCount.toString()