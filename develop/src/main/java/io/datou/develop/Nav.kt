package io.datou.develop

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import io.datou.develop.ScopedNavController.NavigateBuilder

class ScopedNavController(
    val tag: String?,
    val navController: NavHostController,
    val parent: ScopedNavController? = null
) {
    class NavigateBuilder(
        private val navHostController: NavHostController,
        private val router: String
    ) {
        var popUpToStart: Boolean = false
        var popUpToCurrent: Boolean = false
        var inclusive: Boolean = true
        var launchSingleTop: Boolean = false
        var saveState: Boolean = false

        val startDestinationId get() = navHostController.graph.startDestinationId

        val currentDestinationId get() = navHostController.currentDestination!!.id

        internal fun build() {
            navHostController.run {
                val popId =
                    if (popUpToStart) {
                        graph.startDestinationId
                    } else if (popUpToCurrent) {
                        currentBackStackEntry!!.id
                    } else {
                        null
                    }
                navigate(router) {
                    popId?.let {
                        popUpTo(it) {
                            inclusive = this@NavigateBuilder.inclusive
                            saveState = this@NavigateBuilder.saveState
                        }
                        launchSingleTop = this@NavigateBuilder.launchSingleTop
                        restoreState = this@NavigateBuilder.saveState
                    }
                }
            }
        }
    }
}

val ScopedNavController.path: String
    get() {
        var current: ScopedNavController? = this
        val pathSegments = mutableListOf<String>()
        while (current != null) {
            pathSegments.add(current.tag ?: "null")
            current = current.parent
        }
        return pathSegments.asReversed().joinToString("/")
    }

val ScopedNavController.root: ScopedNavController
    get() {
        var current = this
        while (current.parent != null) {
            current = current.parent!!
        }
        return current
    }


fun ScopedNavController.findByTag(tag: String): ScopedNavController? {
    if (this.tag == tag) return this
    return parent?.findByTag(tag)
}

fun ScopedNavController.findByPath(path: String): ScopedNavController? {
    if (this.path == path) return this
    return parent?.findByPath(path)
}

val LocalScopedNavController = staticCompositionLocalOf<ScopedNavController?> {
    null
}

fun ScopedNavController.navigate(router: String, block: (NavigateBuilder.() -> Unit)? = null) {
    NavigateBuilder(navController, router)
        .apply {
            block?.invoke(this)
        }.build()
}

@Composable
fun ScopedNavController(
    tag: String? = null,
    navController: NavHostController = rememberNavController(),
    content: @Composable (NavHostController) -> Unit
) {
    val parent = LocalScopedNavController.current
    CompositionLocalProvider(
        LocalScopedNavController
            .provides(
                ScopedNavController(
                    tag = tag,
                    navController = navController,
                    parent = parent
                )
            ),
    ) {
        content(navController)
    }
}