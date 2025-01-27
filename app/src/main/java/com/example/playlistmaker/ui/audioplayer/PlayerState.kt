package com.example.playlistmaker.ui.audioplayer
enum class PlayerState(
    val id: Int,
) {
    Default(0),
    Prepared(1),
    Playing(2),
    Paused(3),
}