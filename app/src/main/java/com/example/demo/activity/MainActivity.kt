package com.example.demo.activity

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
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
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import io.datou.develop.enableFullScreen
import io.datou.develop.rememberKeyboardHeight


class MainActivity : ComponentActivity() {
    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            navigationBarStyle = SystemBarStyle.dark(Color.RED)
        )
        enableFullScreen()
        setContent {
            Scaffold {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it)
                ) {

                    val a = rememberKeyboardHeight()
                    Log.e("1234","a=$a")
                    Column {
                        Button(
                            modifier = Modifier.statusBarsPadding(),
                            onClick = {
                            }) {
                            Text(text = "点击插入文本", fontSize = 20.sp)
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
}

