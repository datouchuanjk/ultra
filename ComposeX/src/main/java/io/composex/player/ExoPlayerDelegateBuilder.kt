package io.composex.player

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

fun ViewModel.buildExoPlayerDelegate(context: Context): ExoPlayerDelegate {
    return ExoPlayerDelegateImpl(
        context = context,
        scope = viewModelScope
    )
}
