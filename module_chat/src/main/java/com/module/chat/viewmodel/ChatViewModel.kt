package com.module.chat.viewmodel

import com.module.basic.viewmodel.BaseViewModel
import io.composex.paging.buildOffsetPaging
import kotlinx.coroutines.delay

class ChatViewModel :BaseViewModel() {
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