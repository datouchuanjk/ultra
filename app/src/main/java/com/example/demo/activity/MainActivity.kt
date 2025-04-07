package com.example.demo.activity

import android.Manifest
import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import io.datou.develop.enableFullScreen


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
                    Column {
                        Button(
                            modifier = Modifier.statusBarsPadding(),
                            onClick = {
                            }) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                null
                            } else {
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                            }
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                Manifest.permission.READ_MEDIA_IMAGES
                            } else {
                                Manifest.permission.READ_EXTERNAL_STORAGE
                            }
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

