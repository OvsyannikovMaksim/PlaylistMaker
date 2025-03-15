package com.example.playlistmaker.audioplayer.ui

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.audioplayer.domain.MediaPlayerInteractor
import com.example.playlistmaker.audioplayer.domain.model.PlayerState
import com.example.playlistmaker.audioplayer.domain.model.ScreenState

class AudioPlayerViewModel(
    private val mediaPlayerInteractor: MediaPlayerInteractor
) : ViewModel() {
    private val handler = Handler(Looper.getMainLooper())

    private val playerState: MutableLiveData<ScreenState> =
        MutableLiveData(
            ScreenState(
                PlayerState.Default,
                null
            )
        )

    fun getPlayerState(): LiveData<ScreenState> = playerState
    fun prepareMediaPlayer(url: String?) {
        mediaPlayerInteractor.preparePlayer(url,
            {
                playerState.postValue(
                    ScreenState(
                        PlayerState.Prepared,
                        null
                    )
                )
            },
            {
                playerState.postValue(
                    ScreenState(
                        PlayerState.Prepared,
                        null
                    )
                )
                handler.removeCallbacks(updateTimer)
            })
    }

    fun startPlayer() {
        mediaPlayerInteractor.startPlayer()
        handler.post(updateTimer)
    }

    fun pausePlayer() {
        mediaPlayerInteractor.pausePlayer()
        playerState.postValue(
            ScreenState(
                PlayerState.Paused, playerState.value?.currentTime
            )
        )
        handler.removeCallbacks(updateTimer)
    }

    fun releasePlayer() {
        mediaPlayerInteractor.releasePlayer()
        handler.removeCallbacks(updateTimer)
    }

    private val updateTimer =
        object : Runnable {
            override fun run() {
                playerState.postValue(
                    ScreenState(PlayerState.Playing, mediaPlayerInteractor.getCurrentTime())
                )
                handler.postDelayed(this, REFRESH_TIMER_DELAY_MILLIS)
            }
        }

    companion object {
        private const val REFRESH_TIMER_DELAY_MILLIS = 500L
    }
}
