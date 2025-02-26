package com.example.playlistmaker.audioplayer.domain.model

import android.app.Application
import androidx.core.content.ContextCompat
import com.example.playlistmaker.R


data class ScreenState(val playerState: PlayerState, val currentTime: String){

    companion object {
        fun getDefaultScreenState(application: Application): ScreenState {
            return ScreenState(PlayerState.Default,
                ContextCompat.getString(
                    application.applicationContext,
                    R.string.default_current_time
                )
            )
        }

        fun getPreparedScreenState(application: Application): ScreenState{
            return ScreenState(PlayerState.Prepared,
                ContextCompat.getString(
                    application.applicationContext,
                    R.string.default_current_time
                )
            )
        }

        fun getPlayingScreenState(time: String): ScreenState{
            return ScreenState(PlayerState.Playing, time)
        }

        fun getPausedScreenState(time: String): ScreenState{
            return ScreenState(PlayerState.Paused, time)
        }
    }
}