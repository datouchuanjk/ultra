package com.example.demo.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import io.datou.develop.CurrentResumeActivity
import io.datou.develop.StackTopActivity
import io.datou.develop.addSecureFlag
import io.datou.develop.createFileInCacheDir
import io.datou.develop.inputStream
import io.datou.develop.intentOf
import io.datou.develop.toProvideUri
import io.datou.develop.use


class LauncherActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        addSecureFlag()
        setContent {
            val scope = rememberCoroutineScope()
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Button(onClick = {
                    createFileInCacheDir("")
                        .toProvideUri()
                        ?.inputStream()
                        ?.use {
                            it.copyTo()
                        }
                    startActivity(intentOf<MainActivity>())
                }) {
                    Text(text = "to MainActivity", fontSize = 30.sp)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

    }
}