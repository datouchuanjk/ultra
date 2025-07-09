package com.module.basic.module

import com.module.basic.api.UploadApiService
import com.module.basic.http.retrofit2
import org.koin.dsl.module
import retrofit2.Retrofit

val basicModule = module {
    single {
        retrofit2
    }
    single {
        get<Retrofit>().create<UploadApiService>(UploadApiService::class.java)
    }
}