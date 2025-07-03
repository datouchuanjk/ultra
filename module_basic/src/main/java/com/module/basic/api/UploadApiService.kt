package com.module.basic.api

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object UploadApiServiceModule {
    @Provides
    @Singleton
    fun provide(retrofit: Retrofit): UploadApiService {
        return retrofit.create(UploadApiService::class.java)
    }
}

interface UploadApiService {

}