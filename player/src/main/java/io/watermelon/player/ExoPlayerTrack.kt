package io.watermelon.player

import androidx.annotation.OptIn
import androidx.media3.common.Format
import androidx.media3.common.TrackGroup
import androidx.media3.common.util.UnstableApi
import java.math.BigDecimal
import java.math.RoundingMode

data class ExoPlayerTrack(
    internal val index: Int,
    internal val trackGroup: TrackGroup,
    val format: Format,
)

val ExoPlayerTrack.width get() = format.width
val ExoPlayerTrack.height get() = format.height
val ExoPlayerTrack.id get() = format.id
val ExoPlayerTrack.label get() = format.label
val ExoPlayerTrack.bitrate
    @OptIn(UnstableApi::class)
    get() = format.bitrate
val ExoPlayerTrack.language get() = format.language
val ExoPlayerTrack.mimeType get() = format.sampleMimeType
val ExoPlayerTrack.resolution
    get() = takeIf { width != Format.NO_VALUE && height != Format.NO_VALUE }?.let { "${height}p" }

val ExoPlayerTrack.bitrateText
    get() = takeIf { bitrate != Format.NO_VALUE }.let {
        val bd = BigDecimal(bitrate.toDouble() / 1000000)
            .setScale(2, RoundingMode.HALF_UP)
        "${bd}Mbps"
    }
val ExoPlayerTrack.codec: String?
    get() = when {
        mimeType == null -> null
        mimeType!!.contains("avc") -> "H.264"
        mimeType!!.contains("hev") -> "H.265"
        mimeType!!.contains("av01") -> "AV1"
        mimeType!!.contains("vp9") -> "VP9"
        mimeType!!.contains("mp4a") -> "AAC"
        mimeType!!.contains("opus") -> "Opus"
        else -> mimeType
    }