package com.datouchuanjk.player

import androidx.media3.exoplayer.ExoPlayer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

fun buildExoPlayerHelper(
    scope: CoroutineScope,
    player: ExoPlayer
): ExoPlayerHelper {
    return ExoPlayerHelperImpl(
        player = player,
        scope = scope
    )
}
