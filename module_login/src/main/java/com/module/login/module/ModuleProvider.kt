package com.module.login.module

import com.module.login.viewmodel.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val loginModule = module {
    viewModel() {
        LoginViewModel()
    }
}