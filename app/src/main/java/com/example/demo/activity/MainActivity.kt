package com.example.demo.activity

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.launch
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.datou.develop.CustomActivityResultContracts
import io.datou.develop.addSecureFlag
import io.datou.develop.addViewToWindow
import io.datou.develop.intentOf
import io.datou.develop.toast

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        addSecureFlag()
        setContent {
            Scaffold(
            ) { innerPadding ->
                Log.e("1234","innerPadding=$innerPadding")
                LazyColumn(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(15.dp),
                ) {
                    item {
                        Text(
                            text = "ConversationActivity",
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            fontSize = 15.sp, modifier = Modifier
                                .fillParentMaxWidth()
                                .height(50.dp)
                                .background(color = Color.White, shape = RoundedCornerShape(8.dp))
                                .clickable {
                                    startActivity(intentOf<ConversationActivity>())
                                }
                                .wrapContentSize()
                        )
                    }
                    item {
                        val launcher = rememberLauncherForActivityResult(
                            CustomActivityResultContracts.ApplicationDetailsSettings()
                        ) {
                           toast("fuck")
                        }

                        Text(
                            text = "show",
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            fontSize = 15.sp, modifier = Modifier
                                .fillParentMaxWidth()
                                .height(50.dp)
                                .background(color = Color.White, shape = RoundedCornerShape(8.dp))
                                .clickable {
                                    addViewToWindow {
                                        Text(modifier = Modifier.fillMaxWidth().height(200.dp).background(color = Color.Blue), text = "我日你吗吗")
                                    }
                                }
                                .wrapContentSize()
                        )
                    }
                }
            }
        }
    }
}