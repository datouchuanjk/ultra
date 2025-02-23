package io.datou.swipe

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import kotlin.math.roundToInt

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Swipe(
    state: SwipeState,
    modifier: Modifier = Modifier,
    actions: @Composable BoxScope.(Int) -> Unit,
    content: @Composable BoxScope.() -> Unit
) {
    val offset = -state.anchoredDraggableState.requireOffset()
    Box(
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .fillMaxWidth()
                .offset {
                    IntOffset(
                        x = offset.roundToInt(), y = 0
                    )
                }
                .anchoredDraggable(
                    state = state.anchoredDraggableState,
                    orientation = Orientation.Horizontal,
                    reverseDirection = true
                )
        ) {
            content()
        }
        val localDensity = LocalDensity.current
        val itemWidth = remember {
            localDensity.run {
                state.anchoredDraggableState.anchors.maxAnchor().toDp()
            }.run {
                this / state.count
            }
        }
        repeat(state.count) {
            val fraction = 1f - (1f / state.count) * it
            Box(modifier = Modifier
                .align(alignment = Alignment.TopEnd)
                .width(itemWidth)
                .offset {
                    IntOffset(
                        x = (offset * fraction + itemWidth.toPx()).roundToInt(),
                        y = 0
                    )
                }
            ) {
                actions(it)
            }
        }
    }
}