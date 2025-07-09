package com.module.basic.http

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

internal val retrofit2 =  Retrofit.Builder()
    .baseUrl("http://www.baidu.com/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()