package com.module.basic.ds

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore

/**
 * 全局本地存储
 */
val Context.myDataStore by preferencesDataStore("myDataStore")