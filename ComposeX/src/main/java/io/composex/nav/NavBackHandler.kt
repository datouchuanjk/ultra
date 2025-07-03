package io.composex.nav

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import io.composex.util.findActivity

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


