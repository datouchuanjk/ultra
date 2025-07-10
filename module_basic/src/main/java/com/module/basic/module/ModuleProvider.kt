package com.module.basic.module

import android.content.Context
import com.module.basic.api.UploadApiService
import com.module.basic.http.LogInterceptor
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val basicModule = module {
    single {
        val packageName = androidContext().packageName
        androidContext().getSharedPreferences("${packageName}_preferences", Context.MODE_PRIVATE)
    }

    single {
        Retrofit.Builder()
            .baseUrl("http://www.baidu.com/")
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(LogInterceptor())
                    .build()
            ).addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single {
        get<Retrofit>().create(UploadApiService::class.java)
    }
}