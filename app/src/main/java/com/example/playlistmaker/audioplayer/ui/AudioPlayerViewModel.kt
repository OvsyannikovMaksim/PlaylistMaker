package com.example.playlistmaker.audioplayer.ui

import android.app.Application
import android.os.Handler
import android.os.Looper
import androidx.core.content.ContextCompat.getString
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.playlistmaker.R
import com.example.playlistmaker.audioplayer.domain.MediaPlayerInteractor
import com.example.playlistmaker.audioplayer.domain.model.ScreenState

class AudioPlayerViewModel(
    private val application: Application,
    private val mediaPlayerInteractor: MediaPlayerInteractor
) : AndroidViewModel(application) {
    private val handler = Handler(Looper.getMainLooper())

    private val playerState: MutableLiveData<ScreenState> =
        MutableLiveData(ScreenState.getDefaultScreenState(application))

    fun getPlayerState(): LiveData<ScreenState> = playerState
    fun prepareMediaPlayer(url: String?) {
        mediaPlayerInteractor.preparePlayer(url,
            { playerState.postValue(ScreenState.getPreparedScreenState(application)) },
            {
                playerState.postValue(ScreenState.getPreparedScreenState(application))
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
            ScreenState.getPausedScreenState(
                playerState.value?.currentTime ?: getString(
                    application.applicationContext,
                    R.string.default_current_time
                )
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
                    ScreenState.getPlayingScreenState(
                         mediaPlayerInteractor.getCurrentTime()
                    )
                )
                handler.postDelayed(this, REFRESH_TIMER_DELAY_MILLIS)
            }
        }

    companion object {
        private const val REFRESH_TIMER_DELAY_MILLIS = 500L
    }
}
