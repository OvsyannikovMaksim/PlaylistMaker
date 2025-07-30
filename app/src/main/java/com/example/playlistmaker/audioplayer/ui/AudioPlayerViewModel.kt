package com.example.playlistmaker.audioplayer.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.audioplayer.domain.MediaPlayerInteractor
import com.example.playlistmaker.audioplayer.domain.model.PlayerState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AudioPlayerViewModel(
    private val mediaPlayerInteractor: MediaPlayerInteractor
) : ViewModel() {
    private var timerJob: Job? = null

    private val playerState: MutableLiveData<PlayerState> = MutableLiveData(PlayerState.Default)

    fun getPlayerState(): LiveData<PlayerState> = playerState
    fun prepareMediaPlayer(url: String?) {
        mediaPlayerInteractor.preparePlayer(url,
            {
                playerState.postValue(PlayerState.Prepared)
            },
            {
                playerState.postValue(PlayerState.Prepared)
                timerJob?.cancel()
            })
    }

    fun startPlayer() {
        mediaPlayerInteractor.startPlayer()
        playerState.postValue(PlayerState.Playing(mediaPlayerInteractor.getCurrentTime()))
        startTimer()
    }

    fun pausePlayer() {
        mediaPlayerInteractor.pausePlayer()
        timerJob?.cancel()
        playerState.postValue(PlayerState.Paused(mediaPlayerInteractor.getCurrentTime()))
    }

    fun releasePlayer() {
        mediaPlayerInteractor.releasePlayer()
    }

    fun onButtonClick() {
        when (playerState.value) {
            is PlayerState.Paused, is PlayerState.Prepared -> startPlayer()
            is PlayerState.Playing -> pausePlayer()
            else -> Unit
        }
    }

    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (true) {
                delay(REFRESH_TIMER_DELAY_MILLIS)
                playerState.postValue(PlayerState.Playing(mediaPlayerInteractor.getCurrentTime()))
            }
        }
    }

    companion object {
        private const val REFRESH_TIMER_DELAY_MILLIS = 300L
    }
}
