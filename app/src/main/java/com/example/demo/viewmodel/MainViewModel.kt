package com.example.demo.viewmodel

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class MainViewModel:BaseViewModel() {

    val aaa =MutableSharedFlow<String>()

    fun send(){
     viewModelScope.launch {
         aaa.emit("我日")
     }
    }
}