package io.datou.develop

import android.app.Activity
import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Singleton

@AndroidEntryPoint
class TestActivity : Activity() {
    @Inject
    lateinit var a: A
    lateinit var b: B
}

class B @Inject constructor(val a: A)

@HiltViewModel
class  C @Inject constructor(val b:B):ViewModel(){}

@Singleton
class A @Inject constructor()

@HiltAndroidApp
class  D:Application(){}

