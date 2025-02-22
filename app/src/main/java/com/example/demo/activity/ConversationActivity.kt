package com.example.demo.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.demo.activity.ui.HandleViewModel
import com.example.demo.viewmodel.ConversationViewModel
import io.datou.chat.utils.displayStatus
import io.datou.chat.utils.displayText
import io.datou.chat.utils.displayUnreadMsgCount
import io.datou.develop.addSecureFlag

/**
 * 会话列表
 */
class ConversationActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        addSecureFlag()
        setContent {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            Text(
                                text = "ConversationActivity",
                                fontWeight = FontWeight.Bold,
                                color = Color.Black,
                                fontSize = 18.sp
                            )
                        }
                    )
                }
            ) { innerPadding ->
                val viewmodel: ConversationViewModel = viewModel()
                HandleViewModel(viewmodel)
                val list = viewmodel.list
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .background(color = Color(0xff202020)),
                    verticalArrangement = Arrangement.spacedBy(15.dp),
                ) {
                    item {
                        Button(onClick = {
                         viewmodel.send()
                        }) {
                            Text("发给老2")
                        }
                    }

                    items(items = list, key = {
                        it.conversationId()
                    }) {
                        val lastMessage = it.lastMessage
                        ConstraintLayout(
                            modifier = Modifier
                                .fillParentMaxWidth()
                                .padding(horizontal = 13.dp)
                                .height(70.dp)
                                .animateItem()
                        ) {
                            val (image, title, content, time, unreadCount) = createRefs()
                            Box(
                                modifier = Modifier
                                    .constrainAs(image) {
                                        top.linkTo(parent.top)
                                        bottom.linkTo(parent.bottom)
                                        start.linkTo(parent.start)
                                    }
                                    .size(50.dp)
                                    .clip(CircleShape)
                                    .background(color = Color.White)
                            )
                            Text(
                                text = it.conversationId() +it.lastMessage.displayStatus,
                                color = Color.White,
                                fontSize = 15.sp,
                                modifier = Modifier.constrainAs(title) {
                                    top.linkTo(image.top)
                                    start.linkTo(image.end, margin = 13.dp)
                                }
                            )
                            Text(
                                text = lastMessage.displayText,
                                color = Color(0xff999999),
                                fontSize = 13.sp,
                                modifier = Modifier.constrainAs(content) {
                                    top.linkTo(title.bottom, margin = 10.dp)
                                    start.linkTo(title.start)
                                    end.linkTo(unreadCount.start, margin = 15.dp)
                                    width = Dimension.fillToConstraints
                                }
                            )

                            Text(
                                text = it.displayUnreadMsgCount,
                                color = Color.White,
                                fontSize = 10.sp,
                                modifier = Modifier.constrainAs(unreadCount) {
                                    top.linkTo(content.top)
                                    start.linkTo(content.end)
                                    end.linkTo(parent.end)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}