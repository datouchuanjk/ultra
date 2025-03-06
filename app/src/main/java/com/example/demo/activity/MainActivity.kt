package com.example.demo.activity

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import io.datou.develop.ActivityHelper
import io.datou.develop.CustomActivityResultContracts
import io.datou.develop.SPHelper
import io.datou.develop.launchWhenApplicationResumed
import io.datou.develop.intentOf
import io.datou.develop.toast


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Scaffold(
            ) { innerPadding ->
                Log.e("1234", "innerPadding=$innerPadding")
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
                        ActivityHelper.topActivity
                        val launcher = rememberLauncherForActivityResult(
                            CustomActivityResultContracts.ApplicationDetailsSettings()
                        ) {
                            SPHelper.put( "" to 2)
                            toast("fuck")
                        }
                        val localContext = LocalContext.current
                        Text(
                            text = "show",
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            fontSize = 15.sp, modifier = Modifier
                                .fillParentMaxWidth()
                                .height(50.dp)
                                .background(color = Color.White, shape = RoundedCornerShape(8.dp))
                                .clickable {
                                    launchWhenApplicationResumed {

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