package com.datouchuanjk.player

import android.content.Context
import android.net.Uri
import androidx.annotation.OptIn
import androidx.annotation.RawRes
import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DataSpec
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.datasource.RawResourceDataSource
import androidx.media3.exoplayer.dash.DashMediaSource
import androidx.media3.exoplayer.hls.HlsMediaSource
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import java.io.File

@OptIn(UnstableApi::class)
private fun Uri.toMediaSource(context: Context): MediaSource {
    val mediaItem = MediaItem.fromUri(this)
    val dataSourceFactory = DefaultDataSource.Factory(context)
    val extension = when (scheme) {
        "content", "file" -> lastPathSegment?.substringAfterLast('.')
        else -> toString().substringAfterLast('.')
    }
    return when (extension?.lowercase()) {
        "m3u8" -> HlsMediaSource.Factory(dataSourceFactory).createMediaSource(mediaItem)
        "mpd" -> DashMediaSource.Factory(dataSourceFactory).createMediaSource(mediaItem)
        else -> ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(mediaItem)
    }
}

fun ExoPlayerHelper.prepare(context: Context, url: String) {
    prepare(url.toUri().toMediaSource(context))
}

fun ExoPlayerHelper.prepare(context: Context, file: File) {
    prepare(file.toUri().toMediaSource(context))
}

@OptIn(UnstableApi::class)
fun ExoPlayerHelper.prepare(context: Context, @RawRes id: Int) {
    val uri = Uri.Builder()
        .scheme("android.resource")
        .authority(context.packageName)
        .appendPath("raw")
        .appendPath(context.resources.getResourceEntryName(id))
        .build()
    val dataSource = RawResourceDataSource(context)
    dataSource.open(DataSpec(uri))
    val source = ProgressiveMediaSource.Factory { dataSource }
        .createMediaSource(MediaItem.fromUri(uri))
    prepare(source)
}

fun ExoPlayerHelper.seekBy(position: Long) {
    seekTo( currentPosition.value+ position)
}