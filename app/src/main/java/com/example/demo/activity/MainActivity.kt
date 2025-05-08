package com.example.demo.activity

import android.os.Bundle
import android.os.Environment
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ReportFragment.Companion.reportFragment
import androidx.lifecycle.compose.LocalLifecycleOwner
import io.datou.develop.TopActivityOrNull
import io.datou.develop.asFile
import io.datou.develop.currentKeyboardHeightFlow
import io.datou.develop.currentNetworkFlow
import io.datou.develop.enableFullScreen
import io.datou.develop.toFileInDirectory
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.asFlow

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        suspend {
            delay(4000)
            1
        }.asFlow()
        "".asFile()
        enableFullScreen()
        setContent {
            Scaffold {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it)
                ) {
                    Text("123")
                    Spacer(modifier = Modifier.weight(1f))
                    Text("123")
                }
            }
        }
    }
}
