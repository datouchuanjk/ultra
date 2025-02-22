package com.example.demo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.datou.develop.launchSilently
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

abstract class BaseViewModel : ViewModel() {
    private val _loading = MutableStateFlow(0)
    private val _error = MutableSharedFlow<Exception>()

    val loading = _loading.asStateFlow()
    val error = _error.asSharedFlow()

    fun showLoading() {
        _loading.value++
    }

    fun dismissLoading() {
        _loading.value--
    }

    fun showError(exception: Exception) {
        viewModelScope.launchSilently {
            _error.emit(exception)
        }
    }
}