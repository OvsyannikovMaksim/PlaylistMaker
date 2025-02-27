package com.example.playlistmaker.audioplayer.domain.repository

interface MediaPlayerRepository {

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