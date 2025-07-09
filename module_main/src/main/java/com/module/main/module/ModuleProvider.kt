package com.module.main.module

import com.module.main.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mainModule = module {
    viewModel() {
        MainViewModel()
    }
}