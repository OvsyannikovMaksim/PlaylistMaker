package com.example.playlistmaker.media.domain.model

import java.io.Serializable

data class Playlist(
    val id: Int,
    val name: String,
    val desc: String,
    val imagePath: String?,
    val tracksAmount: Int
) : Serializable