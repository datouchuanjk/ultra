package io.datou.develop

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

val LocalNavHostController = staticCompositionLocalOf<NavHostController?> {
    null
}

@Composable
fun LocalNavHostControllerProvider(
    navController: NavHostController = rememberNavController(),
    content: @Composable NavHostController.() -> Unit
) {
    CompositionLocalProvider(
        LocalNavHostController provides navController
    ) {
        content(navController)
    }
}

