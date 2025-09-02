package com.example.playlistmaker.audioplayer.domain

import android.util.Log
import com.example.playlistmaker.audioplayer.domain.repository.MediaPlayerRepository
import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MediaPlayerInteractorImpl(private val mediaPlayerRepository: MediaPlayerRepository) :
    MediaPlayerInteractor {
    override fun preparePlayer(
        url: String?,
        onPreparedListener: () -> Unit,
        onCompletionListener: () -> Unit
    ) {
        mediaPlayerRepository.preparePlayer(url, onPreparedListener, onCompletionListener)
    }

    override fun startPlayer() {
        mediaPlayerRepository.startPlayer()
    }

    override fun pausePlayer() {
       mediaPlayerRepository.pausePlayer()
    }

    override fun releasePlayer() {
        mediaPlayerRepository.releasePlayer()
    }

    override fun getCurrentTime(): String {
        return mediaPlayerRepository.getCurrentTime()
    }

    override suspend fun addToFavourite(track: Track) {
        withContext(Dispatchers.IO) {
            mediaPlayerRepository.addToFavourite(track)
        }
    }

    override suspend fun removeFavourite(track: Track) {
        withContext(Dispatchers.IO) {
            mediaPlayerRepository.removeFavourite(track)
        }
    }

    override suspend fun isTrackInFavourite(track: Track): Boolean {
        return withContext(Dispatchers.IO) {
            mediaPlayerRepository.isTrackInFavourite(track) > 0
        }
    }
}