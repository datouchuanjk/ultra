package com.module.chat.viewmodel

import com.module.basic.viewmodel.BasicViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.composex.paging.buildOffsetPaging
import kotlinx.coroutines.delay
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor() : BasicViewModel() {
    val paging = buildOffsetPaging {
        if (it.key == 1) {
            delay(3000)
            throw  NullPointerException()
            buildList {
                for (i in 1..20) {
                    add("老王$i")
                }
            }
        } else {
            emptyList()
        }
    }
}