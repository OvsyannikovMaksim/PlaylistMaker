package com.example.playlistmaker.audioplayer.domain.model

import com.example.playlistmaker.R

sealed class PlayerState(val id: Int, val currentTime: String, val buttonImageRes: Int) {
    data object Default : PlayerState(0, "00:00", R.drawable.play_button)
    data object Prepared : PlayerState(1, "00:00", R.drawable.play_button)
    data class Playing(val time: String) : PlayerState(2, time, R.drawable.pause_button)
    data class Paused(val time: String) : PlayerState(2, time, R.drawable.play_button)
}
