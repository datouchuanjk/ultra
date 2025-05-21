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
    class NavigateBuilder(private val router: String) {
        var popBack: Boolean = false
        var clear: Boolean = false
        var popUpToInclusive: Boolean = true
        var top: Boolean = false
        var save: Boolean = false
        internal fun NavHostController.build() {
            val popId =
                if (clear) {
                    graph.startDestinationId
                } else if (popBack) {
                    currentBackStackEntry!!.id
                } else {
                    null
                }
            navigate(router) {
                popId?.let {
                    popUpTo(it) {
                        inclusive = popUpToInclusive
                        saveState = save
                    }
                    launchSingleTop = top
                    restoreState = save
                }
            }
        }
    }
}

val ScopedNavController.path: String
    get() = if (parent == null) "$tag" else "${parent.path}/$tag"

val ScopedNavController.root: ScopedNavController
    get() = parent?.root ?: this


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

fun ScopedNavController.navigate(router: String, block: NavigateBuilder.() -> Unit) {
    NavigateBuilder(router).apply(block).apply {
        navController.build()
    }
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