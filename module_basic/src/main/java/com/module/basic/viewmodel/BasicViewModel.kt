package com.module.basic.viewmodel

import androidx.lifecycle.ViewModel
import com.module.basic.http.BusinessException
import com.module.basic.vo.BaseVO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart

abstract class BasicViewModel : ViewModel() {

    private val _errorFlow = MutableSharedFlow<Throwable>()
    val errorFlow = _errorFlow.asSharedFlow()

    private val _loadingFlow = MutableStateFlow(false)
    val loadingFlow = _loadingFlow.asStateFlow()

    fun <T> Flow<BaseVO<T>>.withNullData() = onStart {
        _loadingFlow.value = true
    }.onCompletion {
        _loadingFlow.value = false
    }.map {
        if (it.isSuccessful) {
            it.data
        } else {
            throw BusinessException(
                code = it.code,
                message = it.message
            )
        }
    }.catch {
        _errorFlow.emit(it)
    }

    fun <T> Flow<BaseVO<T>>.withNotNullData() = onStart {
        _loadingFlow.value = true
    }.onCompletion {
        _loadingFlow.value = false
    }.map {
        if (it.isSuccessful) {
            it.data ?: throw NullPointerException()
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