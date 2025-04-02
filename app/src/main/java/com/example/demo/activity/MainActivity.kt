package com.example.demo.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaScannerConnection
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import io.datou.develop.CustomActivityResultContracts
import io.datou.develop.createAbsolutely
import io.datou.develop.newFileInExternalPublicDir


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
                    val launcher =
                        rememberLauncherForActivityResult(CustomActivityResultContracts.ExternalStorageAccessPermission()) {

                        }
                    Button(onClick = {
                        launcher.launch()
                    }) {
                        Text(text = "获取文件管理权限", fontSize = 20.sp)
                    }
                    Button(
                        onClick = {
                            //在android10以上必崩，因为获取了读写权限也没用，必须通过media写入对吧
                            val file = newFileInExternalPublicDir(
                                Environment.DIRECTORY_DOCUMENTS,
                                "fuck1.txt"
                            )
                            Log.e("1234",file.absolutePath)
                            Log.e("1234",Environment.getExternalStoragePublicDirectory("").absolutePath)
                        }) {
                        Text(text = "点击插入文本", fontSize = 20.sp)
                    }
                }
            }
        }
    }


}

