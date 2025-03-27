package com.example.demo.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.lifecycle.lifecycleScope
import io.datou.develop.queryMediaFiles
import io.datou.develop.saveToMediaStore
import io.datou.develop.toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            Log.e("1234", "权限124333结果$it")
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
//                    queryMediaFiles(
//                        "name_444.txt",
//                        "text/plain",
//                        MediaStore.Files.getContentUri("external"),
//                        true
//                    ).forEach {
//                        Log.e("1234",getFilePathFromContentUriPreQ(this@MainActivity,it).orEmpty())
//                    }
                    saveToMediaStore(
                        "name_444.txt",
                        "text/plain",
                        Environment.DIRECTORY_DOCUMENTS,
                        MediaStore.Files.getContentUri("external"),
                        false
                    ) {
                        it.write("我是狗".toByteArray())
                    }
                }
            }
        }.launch(
            arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
            )
        )
    }

}

