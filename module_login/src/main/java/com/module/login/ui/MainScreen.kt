package com.module.login.ui

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import com.module.basic.ui.withLoadingAndError
import com.module.login.viewmodel.LoginViewModel

@Composable
fun LoginScreen(viewModel: LoginViewModel = hiltViewModel<LoginViewModel>().withLoadingAndError()) {

}