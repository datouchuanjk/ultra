package io.datou.swipe

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.DecayAnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Density

@Composable
fun rememberSwipeState(
    initialValue: Int = 0,
    count: Int,
    maxAnchor: Float,
    positionalThreshold: (Float) -> Float = { it * 0.5f },
    velocityThreshold: Density.() -> Float = { 0f },
    snapAnimationSpec: AnimationSpec<Float> = tween(),
    decayAnimationSpec: DecayAnimationSpec<Float> = rememberSplineBasedDecay(),
    confirmValueChange: (newValue: Int) -> Boolean = { true },
): SwipeState {
    return rememberMutableSwipeState(
        initialValue = initialValue,
        count = count,
        maxAnchor = maxAnchor,
        positionalThreshold = positionalThreshold,
        velocityThreshold = velocityThreshold,
        snapAnimationSpec = snapAnimationSpec,
        decayAnimationSpec = decayAnimationSpec,
        confirmValueChange = confirmValueChange,
    )
}

@OptIn(ExperimentalFoundationApi::class)
interface SwipeState {
    val anchoredDraggableState: AnchoredDraggableState<Int>
    val count: Int
    fun backToInitialAnchor()
}