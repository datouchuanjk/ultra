package com.example.demo.ui

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Call
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import io.datou.develop.LocalNavHostController
import io.datou.develop.findActivity
import kotlinx.coroutines.launch


fun NavGraphBuilder.main() {
    composable("main") {
        MainScreen()
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen() {
    Log.e("1234", "${LocalNavHostController.current}")
    val icons = listOf(
        Icons.Rounded.Home,
        Icons.Rounded.Call,
        Icons.Rounded.Person
    )
    val labels = listOf(
        "Home",
        "Chat",
        "Mine",
    )
    var index by rememberSaveable {
        mutableIntStateOf(0)
    }
    val current: Context = LocalContext.current
    val navController = LocalNavHostController.current

    Scaffold(bottomBar = {
        NavigationBar {
            repeat(3) {
                NavigationBarItem(
                    enabled = true,
                    selected = index == it,
                    onClick = {
                        index = it
                    },
                    icon = { Icon(imageVector = icons[it], contentDescription = null) },
                    label = { Text(labels[it]) }
                )
            }
        }
    }) {
        AnimatedContent(targetState = index) {
            when (it) {
                0 -> MainHomeScreen()
                1 -> MainChatScreen()
                2 -> MainMineScreen()
            }
        }
    }
}
