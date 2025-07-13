package com.datouchuanjk.sticky

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntOffset
import kotlin.math.absoluteValue

@Composable
fun StickyHeader(
    modifier: Modifier = Modifier,
    stickyHeight: Density.()-> Float,
    topContent: @Composable Density.(Float) -> Unit,
    content: @Composable Density.(Float) -> Unit
) {
    val density = LocalDensity.current
    var offset by rememberSaveable {
        mutableFloatStateOf(0f)
    }
    val value = stickyHeight.invoke(density)
    val nestedScrollStickyConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                if (available.y < 0) {
                    if (offset <= -value) {
                        return Offset.Zero
                    } else {
                        val y = (value.absoluteValue - offset.absoluteValue)
                            .coerceAtMost(available.y.absoluteValue)
                        offset += -y
                        return Offset(available.x, -y)
                    }
                }
                return Offset.Zero
            }

            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                if (available.y > 0) {
                    if (offset >= 0f) {
                        return Offset.Zero
                    } else {
                        val y = offset.absoluteValue.coerceAtMost(available.y)
                        offset += y
                        return Offset(available.x, y)
                    }
                }
                return Offset.Zero
            }
        }
    }
    Box(
        modifier = modifier
            .nestedScroll(nestedScrollStickyConnection)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(with(density) { value.toDp() })
                .offset {
                    IntOffset(0, offset.toInt())
                }
        ) {
            density.topContent(offset)
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = with(density) {
                        (value + offset).toDp()
                    })
        ) {
            density.content(offset)
        }
    }
}

