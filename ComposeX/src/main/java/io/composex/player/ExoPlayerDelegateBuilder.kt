package io.composex.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.exoplayer.ExoPlayer

fun ViewModel.buildExoPlayerDelegate(
    player: ExoPlayer
): ExoPlayerDelegate {
    return ExoPlayerDelegateImpl(
        player =player,
        scope = viewModelScope
    )
}
