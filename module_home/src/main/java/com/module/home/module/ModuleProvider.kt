package com.module.home.module

import com.module.home.viewmodel.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val homeModule = module {
    viewModel() {
        HomeViewModel()
    }
}