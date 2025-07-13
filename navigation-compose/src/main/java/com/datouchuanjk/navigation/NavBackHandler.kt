package com.datouchuanjk.navigation

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController

private fun Context.findActivity(): Activity? {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    return null
}

@Composable
fun NavBackHandler(
    navController: NavHostController = LocalNavController.current,
    onBack: (() -> Unit)? = null
) {
    val context = LocalContext.current
    val activity = remember(context) {
        context.findActivity()
    }
    BackHandler {
        if (!navController.popBackStack()) {
            onBack?.invoke() ?: activity?.finish()
        }
    }
}


