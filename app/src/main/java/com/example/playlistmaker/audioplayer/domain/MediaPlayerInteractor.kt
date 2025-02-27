package com.example.playlistmaker.audioplayer.domain

interface MediaPlayerInteractor {
    fun preparePlayer(
        url: String?,
        onPreparedListener: () -> Unit,
        onCompletionListener: () -> Unit
    )
    fun startPlayer()
    fun pausePlayer()
    fun releasePlayer()
    fun getCurrentTime(): String
}