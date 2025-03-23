package io.datou.develop

import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsCompat.Type

fun WindowInsetsCompat.getSystemBarsInsets() = getInsets(Type.systemBars())
fun WindowInsetsCompat.getStatusBarsInsets() = getInsets(Type.statusBars())
fun WindowInsetsCompat.getNavigationBarsInsets() = getInsets(Type.navigationBars())
fun WindowInsetsCompat.getImeInsets() = getInsets(Type.ime())

val WindowInsetsCompat.isSystemBarsVisible get() = isVisible(Type.systemBars())
val WindowInsetsCompat.isStatusBarsVisible get() = isVisible(Type.statusBars())
val WindowInsetsCompat.isNavigationBarsVisible get() = isVisible(Type.navigationBars())
val WindowInsetsCompat.isImeVisible get() = isVisible(Type.ime())

