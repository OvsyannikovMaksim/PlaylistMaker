package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.Track

interface SongsRepository {
    fun searchSong(expression: String): List<Track>
}
