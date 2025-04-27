package io.datou.swipe

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.DecayAnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun rememberMutableSwipeState(
    initialValue: Int = 0,
    count: Int,
    maxAnchor: Float,
    positionalThreshold: (Float) -> Float = { it * 0.5f },
    velocityThreshold: Density.() -> Float = { 0f },
    snapAnimationSpec: AnimationSpec<Float> = tween(),
    decayAnimationSpec: DecayAnimationSpec<Float> = rememberSplineBasedDecay(),
    confirmValueChange: (newValue: Int) -> Boolean = { true },
): SwipeState {
    val density = LocalDensity.current
    val state = remember(
        initialValue,
        maxAnchor,
        positionalThreshold,
        velocityThreshold,
        snapAnimationSpec,
        decayAnimationSpec,
        confirmValueChange,
    ) {
        AnchoredDraggableState(
            initialValue = initialValue,
            anchors = DraggableAnchors {
                0 at 0f
                1 at maxAnchor
            },
            positionalThreshold = positionalThreshold,
            velocityThreshold = { density.velocityThreshold() },
            snapAnimationSpec = snapAnimationSpec,
            decayAnimationSpec = decayAnimationSpec,
            confirmValueChange = confirmValueChange
        )
    }
    val scope = rememberCoroutineScope()
    return remember(state, scope, count) {
        MutableSwipeState(state, count, scope)
    }
}

@ExperimentalFoundationApi
internal class MutableSwipeState(
    override val anchoredDraggableState: AnchoredDraggableState<Int>,
    override val count: Int,
    private val scope: CoroutineScope,
) : SwipeState {
    override fun backToInitialAnchor() {
        scope.launch {
            anchoredDraggableState.animateTo(0)
        }
    }
}