package com.module.main.viewmodel

import com.module.basic.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : BaseViewModel() {

    private val _index = MutableStateFlow(0)
    val index = _index.asStateFlow()
    fun index(index: Int) {
        _index.value = index
    }
}