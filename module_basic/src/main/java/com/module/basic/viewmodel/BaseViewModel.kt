package com.module.basic.viewmodel

import androidx.lifecycle.ViewModel
import com.module.basic.http.BusinessException
import com.module.basic.entry.BaseEntry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart

abstract class BaseViewModel : ViewModel() {

    private val _errorFlow = MutableSharedFlow<Throwable>()
    internal val errorFlow = _errorFlow.asSharedFlow()

    private val _loadingFlow = MutableStateFlow(false)
    internal val loadingFlow = _loadingFlow.asStateFlow()

    fun <T> Flow<BaseEntry<T>>.withNullData() = transitionResult {
        it.data
    }

    fun <T> Flow<BaseEntry<T>>.withNotNullData() = transitionResult {
        it.data ?: throw NullPointerException()
    }

    private fun <T> Flow<BaseEntry<T>>.transitionResult(
        map: suspend (BaseEntry<T>) -> Unit
    ) = onStart {
        _loadingFlow.value = true
    }.onCompletion {
        _loadingFlow.value = false
    }.map {
        if (it.isSuccessful) {
            map(it)
        } else {
            throw BusinessException(
                code = it.code,
                message = it.message
            )
        }
    }.catch {
        _errorFlow.emit(it)
    }
}