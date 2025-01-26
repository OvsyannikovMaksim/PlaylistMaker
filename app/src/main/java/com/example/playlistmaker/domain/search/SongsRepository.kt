package com.example.playlistmaker.domain.search

import com.example.playlistmaker.domain.search.model.Track

interface SongsRepository {
    fun searchSong(expression: String): List<Track>
}
