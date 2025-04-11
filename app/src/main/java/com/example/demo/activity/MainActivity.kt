package com.example.demo.activity

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import io.datou.develop.commitInt
import io.datou.develop.createDesignAdapterContext
import io.datou.develop.shareTextTo


class MainActivity : ComponentActivity() {
    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Scaffold {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it)
                ) {
                    CONNECTIVITY_SERVICE. commitInt(1)
                    Column {
                        Button(
                            modifier = Modifier.statusBarsPadding(),
                            onClick = {
                                shareTextTo("123")
                            }) {
                            Text(text = "share", fontSize = 20.sp)
                        }
                        OutlinedTextField(value = "", onValueChange = {})
                        Spacer(modifier = Modifier.weight(1f))
                        Button(
                            modifier = Modifier.navigationBarsPadding(),
                            onClick = {
                            }) {
                            Text(text = "点击插入文本", fontSize = 20.sp)
                        }
                    }
                }
            }
        }
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase?.createDesignAdapterContext(375f))
    }
}

