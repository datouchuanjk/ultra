package com.module.basic.module

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.preferencesDataStoreFile
import com.module.basic.api.UploadApiService
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val basicModule = module {
    single {
        PreferenceDataStoreFactory.create {
            androidContext().preferencesDataStoreFile("appDataStore")
        }
    }
    single {
        GsonConverterFactory.create()
    }
    single {
        Retrofit.Builder()
            .baseUrl("http://www.baidu.com/")
            .addConverterFactory(get())
            .build()
    }

    single {
        get<Retrofit>().create(UploadApiService::class.java)
    }
}