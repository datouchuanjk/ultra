package io.composex.player

import androidx.media3.common.PlaybackException
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.MediaSource
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface ExoPlayerDelegate  {
    val duration: StateFlow<Long>
    val isBuffering: StateFlow<Boolean>
    val isPlaying: StateFlow<Boolean>
    val isLoading: StateFlow<Boolean>
    val isPlayEnded: StateFlow<Boolean>
    val isRenderedFirstFrame: StateFlow<Boolean>
    val bufferedPosition: StateFlow<Long>
    val bufferedPercentage: StateFlow<Int>
    val currentPosition: StateFlow<Long>
    val currentPercentage: StateFlow<Int>
    val error: SharedFlow<PlaybackException>
    val playbackSpeed: StateFlow<Float>
    val volume: StateFlow<Float>
    val currentVideoTracks: StateFlow<List<ExoPlayerTrack>>
    val currentAudioTracks: StateFlow<List<ExoPlayerTrack>>
    val currentTextTracks: StateFlow<List<ExoPlayerTrack>>
    val currentUnknownTrack: StateFlow<List<ExoPlayerTrack>>
    val currentSelectedVideoTrack: StateFlow<ExoPlayerTrack?>
    val currentSelectedAudioExoPlayerTrack: StateFlow<ExoPlayerTrack?>
    val currentSelectedTextTrack: StateFlow<ExoPlayerTrack?>
    fun prepare(source: MediaSource)
    fun play()
    fun pause()
    fun seekTo(position: Long)
    fun playbackSpeed(speed: Float)
    fun volume(volume: Float)
    fun track(track: ExoPlayerTrack)
}




