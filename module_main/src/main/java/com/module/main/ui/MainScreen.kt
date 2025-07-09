package com.module.main.ui

import android.content.Context
import android.net.Uri
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.module.chat.ui.ChatScreen
import com.module.home.ui.HomeScreen
import com.module.main.viewmodel.MainViewModel
import com.module.mine.ui.MineScreen
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainScreen(viewModel: MainViewModel  = koinViewModel()) {
    val items = listOf(
        "home" to Icons.Default.Home,
        "chat" to Icons.Default.Call,
        "mine" to Icons.Default.Face,
    )
    val selectedIndex by viewModel.index.collectAsState()
    Scaffold(
        bottomBar = {
            NavigationBar {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = selectedIndex == index,
                        label = {
                            Text(item.first)
                        },
                        icon = {
                            Icon(item.second, contentDescription = null)
                        },
                        onClick = {
                            viewModel.index(index)
                        }
                    )
                }
            }
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            val stateHolder = rememberSaveableStateHolder()
            when (selectedIndex) {
                0 -> stateHolder.SaveableStateProvider("home") { HomeScreen() }
                1 -> stateHolder.SaveableStateProvider("chat") { ChatScreen() }
                2 -> stateHolder.SaveableStateProvider("mine") { MineScreen() }
            }
        }
    }
}
