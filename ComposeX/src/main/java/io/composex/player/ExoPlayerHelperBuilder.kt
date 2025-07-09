package io.composex.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.exoplayer.ExoPlayer

fun ViewModel.buildExoPlayerHelper(
    player: ExoPlayer
): ExoPlayerHelper {
    return ExoPlayerHelperImpl(
        player = player,
        scope = viewModelScope
    )
}
