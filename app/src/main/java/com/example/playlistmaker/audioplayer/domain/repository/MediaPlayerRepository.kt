package com.example.playlistmaker.audioplayer.domain.repository

import com.example.playlistmaker.search.domain.model.Track

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
    suspend fun addToFavourite(track: Track)
    suspend fun removeFavourite(track: Track)

    suspend fun isTrackInFavourite(track: Track): Int
}