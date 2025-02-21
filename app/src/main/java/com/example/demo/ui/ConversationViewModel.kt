package com.example.demo.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hyphenate.chat.EMConversation
import io.datou.chat.helper.ConversationHelper
import io.datou.chat.helper.IMHelper
import io.datou.chat.listener.ConversationListener
import io.datou.develop.launchSilently
import io.datou.develop.removeIf
import io.datou.develop.replaceIf
import io.datou.develop.toast

const val APP_KEY = "1199221128116146#demo"
const val USER_NAME = "user2"
const val PWD = "123456"

class ConversationViewModel : ViewModel(), ConversationListener {

    private val _list = mutableStateListOf<EMConversation>()
    private var _isNoMore by mutableStateOf(false)
    private val _conversationHelper = ConversationHelper()

    val list: List<EMConversation> get() = _list
    val isNoMore get() = _isNoMore

    init {
        viewModelScope.launchSilently {
            IMHelper.init(APP_KEY)
            IMHelper.login(USER_NAME, PWD)
            IMHelper.addConversationListener(this@ConversationViewModel)
            refresh()
        }.invokeOnCompletion {
            it?.let {
                toast(it.message)
            }
        }
    }

    fun refresh() {
        viewModelScope.launchSilently {
            _list.clear()
            _list.addAll(_conversationHelper.refresh())
            _isNoMore = _conversationHelper.isNoMore
        }.invokeOnCompletion {
            it?.let {
                toast(it.message)
            }
        }
    }

    fun loadMore() {
        viewModelScope.launchSilently {
            _list.addAll(_conversationHelper.loadMore())
            _isNoMore = _conversationHelper.isNoMore
        }.invokeOnCompletion {
            it?.let {
                toast(it.message)
            }
        }
    }

    override fun onConversationUpdate(
        conversation: EMConversation,
        type: ConversationListener.Type
    ) {
        when (type) {
            ConversationListener.Type.CONVERSATION_READ_ASK,
            ConversationListener.Type.CONVERSATION_MARK_READ,
            ConversationListener.Type.MESSAGE_READ_ASK,
            ConversationListener.Type.MESSAGE_MARK_READ,
            ConversationListener.Type.MESSAGE_DELIVERED_ASK,
            ConversationListener.Type.MESSAGE_RECEIVED,
            ConversationListener.Type.MESSAGE_SENDING,
            ConversationListener.Type.MESSAGE_SEND_SUCCESS,
            ConversationListener.Type.MESSAGE_SEND_FAILED -> {
                _list.replaceIf(conversation) {
                    it.conversationId() == conversation.conversationId()
                }
            }

            ConversationListener.Type.NEW_CONVERSATION -> {
                _list.add(0, conversation)
            }

            ConversationListener.Type.DELETE_CONVERSATION -> {
                _list.removeIf {
                    it.conversationId() == conversation.conversationId()
                }
            }
        }
    }
}