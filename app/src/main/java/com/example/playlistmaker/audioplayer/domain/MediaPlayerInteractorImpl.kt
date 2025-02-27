package com.example.playlistmaker.audioplayer.domain

import com.example.playlistmaker.audioplayer.domain.repository.MediaPlayerRepository

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

}