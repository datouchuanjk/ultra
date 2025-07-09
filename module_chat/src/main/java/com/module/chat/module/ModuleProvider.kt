package com.module.chat.module

import com.module.chat.viewmodel.ChatViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val chatModule = module {
    viewModel() {
        ChatViewModel()
    }
}