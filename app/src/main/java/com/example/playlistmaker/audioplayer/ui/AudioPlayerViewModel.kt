package com.example.playlistmaker.audioplayer.ui

import android.app.Application
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import androidx.core.content.ContextCompat.getString
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.playlistmaker.R
import com.example.playlistmaker.audioplayer.domain.model.ScreenState
import com.example.playlistmaker.utils.Utils

class AudioPlayerViewModel(
    private val application: Application,
) : AndroidViewModel(application) {
    private var mediaPlayer = MediaPlayer()
    private val handler = Handler(Looper.getMainLooper())

    private val playerState: MutableLiveData<ScreenState> =
        MutableLiveData(ScreenState.getDefaultScreenState(application))

    fun getPlayerState(): LiveData<ScreenState> = playerState
    fun prepareMediaPlayer(url: String?) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState.postValue(ScreenState.getPreparedScreenState(application))
        }
        mediaPlayer.setOnCompletionListener {
            playerState.postValue(ScreenState.getPreparedScreenState(application))
            handler.removeCallbacks(updateTimer)
        }
    }

    fun startPlayer() {
        mediaPlayer.start()
        handler.post(updateTimer)
    }

    fun pausePlayer() {
        mediaPlayer.pause()
        playerState.postValue(
            ScreenState.getPausedScreenState(
                playerState.value?.currentTime ?: getString(application.applicationContext, R.string.default_current_time)
            )
        )
        handler.removeCallbacks(updateTimer)
    }

    fun releasePlayer() {
        mediaPlayer.release()
        handler.removeCallbacks(updateTimer)
    }

    private val updateTimer =
        object : Runnable {
            override fun run() {
                playerState.postValue(
                    ScreenState.getPlayingScreenState(
                        Utils.timeConverter(
                            mediaPlayer.currentPosition.toLong()
                        )
                    )
                )
                handler.postDelayed(this, REFRESH_TIMER_DELAY_MILLIS)
            }
        }

    companion object {
        private const val REFRESH_TIMER_DELAY_MILLIS = 500L
    }
}
