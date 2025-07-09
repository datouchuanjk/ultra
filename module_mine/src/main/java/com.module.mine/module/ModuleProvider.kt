package com.module.mine.module

import com.module.mine.viewmodel.MineViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mineModule = module {
    viewModel() {
        MineViewModel()
    }
}