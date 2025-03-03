package com.example.playlistmaker.audioplayer.data

import android.media.MediaPlayer
import com.example.playlistmaker.audioplayer.domain.repository.MediaPlayerRepository
import com.example.playlistmaker.utils.Utils

class MediaPlayerRepositoryImpl(private val mediaPlayer: MediaPlayer) : MediaPlayerRepository {

    override fun preparePlayer(
        url: String?,
        onPreparedListener: () -> Unit,
        onCompletionListener: () -> Unit
    ) {
        mediaPlayer.apply {
            setDataSource(url)
            prepareAsync()
            setOnPreparedListener { onPreparedListener() }
            setOnCompletionListener { onCompletionListener() }
        }
    }

    override fun startPlayer() {
        mediaPlayer.start()
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
    }

    override fun releasePlayer() {
        mediaPlayer.release()
    }

    override fun getCurrentTime(): String {
        return Utils.timeConverter(mediaPlayer.currentPosition.toLong())
    }
}