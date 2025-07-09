package com.module.main.viewmodel

import com.module.basic.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel  : BaseViewModel() {
    private val _index = MutableStateFlow(0)
    val index = _index.asStateFlow()
    fun index(index: Int) {
        _index.value = index
    }
}

