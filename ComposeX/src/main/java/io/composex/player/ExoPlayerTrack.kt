package io.composex.player

import androidx.annotation.OptIn
import androidx.media3.common.Format
import androidx.media3.common.TrackGroup
import androidx.media3.common.util.UnstableApi
import java.math.BigDecimal
import java.math.RoundingMode

data class ExoPlayerTrack(
    internal val index: Int,
    internal val trackGroup: TrackGroup,
) {
    val format
        @OptIn(UnstableApi::class)
        get() = trackGroup.getFormat(index)
    val width get() = format.width
    val height get() = format.height
    val id get() = format.id
    val label get() = format.label
    val bitrate
        @OptIn(UnstableApi::class)
        get() = format.bitrate
    val language get() = format.language
    val mimeType get() = format.sampleMimeType
    val resolution: String
        get() = if (width != Format.NO_VALUE && height != Format.NO_VALUE) {
            "${height}p"
        } else {
            "unit"
        }
    val bitrateText: String
        get() = if (bitrate != Format.NO_VALUE) {
            val bd = BigDecimal(bitrate.toDouble() / 1000000)
                .setScale(2, RoundingMode.HALF_UP)
            "$bd Mbps"
        } else {
            "unit"
        }
    val codec: String?
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
}
