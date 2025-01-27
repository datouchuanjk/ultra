package com.example.demo

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.standard.tools.Activities
import io.standard.tools.PeekActivity
import io.standard.tools.killAllActivities


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        Activities
        PeekActivity
        killAllActivities()
        setContent {
            Scaffold(
                modifier = Modifier
                    .fillMaxSize()
            ) { innerPadding ->
                Column(
                    modifier = Modifier
                        .background(Color.White)
                        .padding(innerPadding)
                        .fillMaxSize()
                ) {
                    Text(text = "", fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(15.dp))
                    Image(
                        painter = painterResource(R.drawable.ic_launcher),
                        contentDescription = null,
                        modifier = Modifier.clip(CircleShape)
                    )
                }
            }
        }
    }
}






