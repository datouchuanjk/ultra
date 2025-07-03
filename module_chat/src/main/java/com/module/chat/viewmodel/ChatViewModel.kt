package com.module.chat.viewmodel

import com.module.basic.viewmodel.BasicViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.composex.paging.buildOffsetPaging
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor() : BasicViewModel() {
    val paging = buildOffsetPaging {
        if (it.key == 1) {
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