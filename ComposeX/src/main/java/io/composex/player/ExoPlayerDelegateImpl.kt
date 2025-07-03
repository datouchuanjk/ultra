package io.composex.player

import android.content.Context
import androidx.media3.common.C
import androidx.media3.common.PlaybackException
import androidx.media3.common.PlaybackParameters
import androidx.media3.common.Player
import androidx.media3.common.TrackSelectionOverride
import androidx.media3.common.TrackSelectionParameters
import androidx.media3.common.Tracks
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.MediaSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

internal class ExoPlayerDelegateImpl(
    val context: Context,
    private val scope: CoroutineScope
) : ExoPlayerDelegate, Player.Listener,
    CoroutineScope by scope {
    override val player: ExoPlayer = ExoPlayer
        .Builder(context)
        .build()
    override val duration = MutableStateFlow(0L)
    override val isBuffering = MutableStateFlow(false)
    override val isPlaying = MutableStateFlow(false)
    override val isLoading = MutableStateFlow(false)
    override val isPlayEnded = MutableStateFlow(false)
    override val isRenderedFirstFrame = MutableStateFlow(false)
    override val bufferedPosition = MutableStateFlow(0L)
    override val bufferedPercentage = MutableStateFlow(0)
    override val currentPosition = MutableStateFlow(0L)
    override val currentPercentage = MutableStateFlow(0)
    override val error = MutableSharedFlow<PlaybackException>()
    override val playbackSpeed = MutableStateFlow(player.playbackParameters.speed)
    override val volume = MutableStateFlow(player.volume)
    override val currentVideoTracks = MutableStateFlow(listOf<ExoPlayerTrack>())
    override val currentAudioTracks = MutableStateFlow(listOf<ExoPlayerTrack>())
    override val currentTextTracks = MutableStateFlow(listOf<ExoPlayerTrack>())
    override val currentUnknownTrack = MutableStateFlow(listOf<ExoPlayerTrack>())
    override val currentSelectedVideoTrack = MutableStateFlow<ExoPlayerTrack?>(null)
    override val currentSelectedAudioExoPlayerTrack = MutableStateFlow<ExoPlayerTrack?>(null)
    override val currentSelectedTextTrack = MutableStateFlow<ExoPlayerTrack?>(null)
    private var _currentPollingJob: Job? = null
    private var _bufferedPollingJob: Job? = null
    private var _bufferedPollingNoChangeCount = 0

    init {
        player.addListener(this)
        scope.coroutineContext[Job]?.invokeOnCompletion {
            player.removeListener(this)
            player.stop()
            player.release()
        }
    }

    init {
        launch {
            isLoading
                .collect {
                    _bufferedPollingJob?.cancel()
                    _bufferedPollingJob = launch {
                        while (isActive) {
                            if (bufferedPercentage.value == player.bufferedPercentage) {
                                if (_bufferedPollingNoChangeCount++ > 10) {
                                    _bufferedPollingJob?.cancel()
                                    _bufferedPollingJob = null
                                }
                            } else {
                                _bufferedPollingNoChangeCount = 0
                            }
                            bufferedPosition.value = player.bufferedPosition
                            bufferedPercentage.value = player.bufferedPercentage
                            delay(100)
                        }
                    }
                }
        }
    }

    init {
        launch {
            isPlaying
                .collect {
                    if (it) {
                        _currentPollingJob?.cancel()
                        _currentPollingJob = launch {
                            while (isActive) {
                                currentPosition.value = player.currentPosition
                                currentPercentage.value = player.run {
                                    (currentPosition * 1f / duration * 100f).toInt()
                                }
                                delay(100)
                            }
                        }
                    } else {
                        _currentPollingJob?.cancel()
                        _currentPollingJob = null
                    }
                }
        }
    }

    @OptIn(UnstableApi::class)
    override fun prepare(block: (Context) -> MediaSource) {
        player.setMediaSource(block(context))
        player.playWhenReady = false
        player.prepare()
    }

    override fun play() {
        player.playWhenReady = true
        if (player.currentPosition >= player.duration - 1) {
            player.seekToDefaultPosition()
        }
        isPlayEnded.value = false
    }

    override fun pause() {
        player.playWhenReady = false
    }

    override fun seekTo(position: Long) {
        if (player.currentPosition >= player.duration - 1) {
            isPlayEnded.value = true
            return
        }
        player.seekTo(position.coerceIn(0L, player.duration))
    }

    override fun playbackSpeed(speed: Float) {
        player.playbackParameters = PlaybackParameters(speed)
    }

    override fun volume(volume: Float) {
        player.volume = volume
    }

    override fun track(track: ExoPlayerTrack) {
        player.trackSelectionParameters = player.trackSelectionParameters.buildUpon()
            .setOverrideForType(
                TrackSelectionOverride(
                    track.trackGroup,
                    track.index
                )
            ).build()
    }

    override fun onIsPlayingChanged(isPlaying: Boolean) {
        super.onIsPlayingChanged(isPlaying)
        this.isPlaying.value = isPlaying
    }

    override fun onIsLoadingChanged(isLoading: Boolean) {
        super.onIsLoadingChanged(isLoading)
        this.isLoading.value = isLoading
    }

    override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters) {
        super.onPlaybackParametersChanged(playbackParameters)
        this.playbackSpeed.value = playbackParameters.speed
    }

    override fun onVolumeChanged(volume: Float) {
        super.onVolumeChanged(volume)
        this.volume.value = volume
    }

    override fun onRenderedFirstFrame() {
        super.onRenderedFirstFrame()
        isRenderedFirstFrame.value = true
    }

    override fun onPlayerError(error: PlaybackException) {
        super.onPlayerError(error)
        launch {
            this@ExoPlayerDelegateImpl.error.emit(error)
        }
    }

    override fun onPlaybackStateChanged(playbackState: Int) {
        super.onPlaybackStateChanged(playbackState)
        when (playbackState) {
            Player.STATE_READY -> {
                duration.value = player.duration
                isBuffering.value = false
            }

            Player.STATE_IDLE -> {
            }

            Player.STATE_ENDED -> {
                isPlayEnded.value = true
            }

            Player.STATE_BUFFERING -> {
                isBuffering.value = true
            }
        }
    }

    override fun onTrackSelectionParametersChanged(parameters: TrackSelectionParameters) {
        super.onTrackSelectionParametersChanged(parameters)
        updateTracks()
    }

    @OptIn(UnstableApi::class)
    override fun onTracksChanged(tracks: Tracks) {
        super.onTracksChanged(tracks)
        updateTracks(tracks)
    }

    @OptIn(UnstableApi::class)
    private fun updateTracks(tracks: Tracks = player.currentTracks) {
        val videoTracks = mutableListOf<ExoPlayerTrack>()
        val audioTracks = mutableListOf<ExoPlayerTrack>()
        val textTracks = mutableListOf<ExoPlayerTrack>()
        val unknownTracks = mutableListOf<ExoPlayerTrack>()
        tracks.groups.forEach { group ->
            val trackGroup = group.mediaTrackGroup
            for (index in 0 until trackGroup.length) {
                val track = ExoPlayerTrack(index, trackGroup)
                when (group.type) {
                    C.TRACK_TYPE_VIDEO -> {
                        videoTracks.add(track)
                        if (group.isTrackSelected(index)) {
                            currentSelectedVideoTrack.value = track
                        }
                    }

                    C.TRACK_TYPE_AUDIO -> {
                        audioTracks.add(track)
                        if (group.isTrackSelected(index)) {
                            currentSelectedAudioExoPlayerTrack.value = track
                        }
                    }

                    C.TRACK_TYPE_TEXT -> {
                        textTracks.add(track)
                        if (group.isTrackSelected(index)) {
                            currentSelectedTextTrack.value = track
                        }
                    }

                    else -> unknownTracks.add(track)
                }
            }
        }
        currentVideoTracks.value = videoTracks
        currentAudioTracks.value = audioTracks
        currentTextTracks.value = textTracks
        currentUnknownTrack.value = unknownTracks
    }
}
