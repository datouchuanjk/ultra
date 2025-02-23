package com.example.demo.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.viewModelScope
import com.hyphenate.chat.EMConversation
import com.hyphenate.chat.EMMessage
import io.datou.chat.helper.ChatHelper
import io.datou.chat.listener.ChatConversationListener
import io.datou.chat.utils.send
import io.datou.develop.launchSilently
import io.datou.develop.toast

class ConversationViewModel : BaseViewModel() {

    private val _list = mutableStateListOf<EMConversation>()
    private val _listener = ChatConversationListener(_list)
    val list: List<EMConversation> get() = _list

    init {
        ChatHelper.addListener(_listener)
        refresh()
    }

    override fun onCleared() {
        super.onCleared()
        ChatHelper.removeListener(_listener)
    }

    fun send() {
        EMMessage
            .createTextSendMessage("你好老2 ${System.currentTimeMillis()}", "user2")
            .send()
    }

    private fun refresh() {
        showLoading()
        viewModelScope.launchSilently {
            _list.clear()
            _list.addAll(ChatHelper.getConversationsFromDB().data)
        }.invokeOnCompletion {
            dismissLoading()
            it?.let {
                toast(it.message)
            }
        }
    }
}