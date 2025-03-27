package com.example.demo.activity

import android.app.Application
import io.datou.develop.startDevelop


class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startDevelop()
    }
}