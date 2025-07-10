package com.module.login.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.module.basic.ui.AppTopBar
import com.module.login.R
import com.module.login.viewmodel.LoginViewModel

@Composable
fun LoginScreen(viewModel: LoginViewModel = viewModel()) {
    Scaffold(
        topBar = {
            AppTopBar(stringResource(R.string.login))
        }
    ) {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(it)) {

        }
    }
}