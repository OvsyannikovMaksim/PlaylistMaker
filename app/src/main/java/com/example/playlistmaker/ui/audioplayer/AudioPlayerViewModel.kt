package com.example.playlistmaker.ui.audioplayer

import android.app.Application
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import androidx.core.content.ContextCompat.getString
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.playlistmaker.R
import com.example.playlistmaker.utils.Utils

class AudioPlayerViewModel(private val application: Application) : AndroidViewModel(application) {

    private var mediaPlayer = MediaPlayer()
    private val handler = Handler(Looper.getMainLooper())

    private val playerState: MutableLiveData<PlayerState> = MutableLiveData(PlayerState.Default)
    private val currentTrackTime: MutableLiveData<String> = MutableLiveData()
    fun getPlayerState(): LiveData<PlayerState> = playerState
    fun getCurrentTrackTime(): LiveData<String> = currentTrackTime

    fun prepareMediaPlayer(url: String?) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState.postValue(PlayerState.Prepared)
        }
        mediaPlayer.setOnCompletionListener {
            playerState.postValue(PlayerState.Prepared)
            handler.removeCallbacks(updateTimer)
            currentTrackTime.postValue(getString(application.applicationContext,R.string.default_current_time))
        }
    }

    fun startPlayer(){
        mediaPlayer.start()
        playerState.postValue(PlayerState.Playing)
        handler.post(updateTimer)
    }

    fun pausePlayer(){
        mediaPlayer.pause()
        playerState.postValue(PlayerState.Paused)
        handler.removeCallbacks(updateTimer)
    }

    fun releasePlayer() {
        mediaPlayer.release()
        handler.removeCallbacks(updateTimer)
    }

    private val updateTimer =
        object : Runnable {
            override fun run() {
                currentTrackTime.postValue(Utils.timeConverter(mediaPlayer.currentPosition.toLong()))
                handler.postDelayed(this, REFRESH_TIMER_DELAY_MILLIS)
            }
        }

    companion object {
        private const val REFRESH_TIMER_DELAY_MILLIS = 500L
    }
}