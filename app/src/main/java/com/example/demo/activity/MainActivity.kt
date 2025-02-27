package com.example.demo.activity

import android.os.Bundle
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
import io.datou.develop.intentOf
import io.datou.develop.toast

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        addSecureFlag()
        setContent {
            Scaffold(
                topBar = {
                    Column {
                        TopAppBar(
                            title = {
                                Text(
                                    text = "MainActivity",
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black,
                                    fontSize = 18.sp
                                )
                            }
                        )
                        HorizontalDivider()
                    }
                }
            ) { innerPadding ->
                val scope = rememberCoroutineScope()
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(vertical = 15.dp, horizontal = 15.dp),
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
                            CustomActivityResultContracts.ManageOverlayPermissions()
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
                                    launcher.launch()
                                }
                                .wrapContentSize()
                        )
                    }
                }
            }
        }
    }
}