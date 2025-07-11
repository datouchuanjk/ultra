package io.watermelon.nav

import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch

fun NavHostController.navigateTo(route: String) {
    val currentRoute: String? = currentDestination?.route
    navigate(route) {
        currentRoute?.let {
            popUpTo(it) {
                inclusive = true
            }
        }
    }
}

fun <T> NavHostController.navigateForResult(route: String, block: (T?) -> Unit) {
    val lastBackStackEntry = currentBackStackEntry
    navigate(route)
    lastBackStackEntry?.lifecycleScope?.launch {
        currentBackStackEntryFlow.filter {
            it == lastBackStackEntry
        }.take(1)
            .map {
                currentBackStackEntry?.savedStateHandle
            }.collect {
                try {
                    val result = it?.get<T>("POP_RESULT")
                    it?.remove<T>("POP_RESULT")
                    block(result)
                } catch (e: Exception) {
                    e.printStackTrace()
                    block(null)
                }
            }
    }
}

fun <T> NavHostController.setResult(value: T) {
    previousBackStackEntry?.savedStateHandle?.set("POP_RESULT", value)
}

fun NavHostController.popToOrNavigate(route: String) {
    if (!popBackStack(route = route, inclusive = false)) {
        navigate(route) {
            launchSingleTop = true
            popUpTo(0)
        }
    }
}

