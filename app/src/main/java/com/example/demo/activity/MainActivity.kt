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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.example.demo.R
import io.datou.develop.Instance
import io.datou.develop.data
import io.datou.develop.delete
import io.datou.develop.deleteFromMediaStore
import io.datou.develop.displayName
import io.datou.develop.insertToMediaStore
import io.datou.develop.mimeType
import io.datou.develop.queryFromMediaStore
import io.datou.develop.relativePath
import io.datou.develop.setDisplayNameAndInferMimeType
import io.datou.develop.setFilePathCompat
import io.datou.develop.toast
import java.io.File


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
                    Button(onClick = {
                        insetTxt()
                    }) {
                        Text(text = "点击插入文本", fontSize = 20.sp)
                    }
                }
            }
        }
    }


    private fun insetTxt() {
        ContentValues()
            .setDisplayNameAndInferMimeType("a.txt")
            .deleteFromMediaStore {
                ContentValues()
                    .setDisplayNameAndInferMimeType("a.txt")
                    .setFilePathCompat(Environment.DIRECTORY_DOCUMENTS)
                    .insertToMediaStore {
                        it.write("12".toByteArray())
                    }?.apply {
                        val a =
                            "displayName=${displayName} \nmimeType=${mimeType}\nrelativePath=$relativePath\ndata=$data"
                        Log.e("124", a)
                    }
            }

    }
}

