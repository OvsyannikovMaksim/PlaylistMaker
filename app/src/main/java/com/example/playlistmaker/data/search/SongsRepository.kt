package com.example.playlistmaker.data.search

import com.example.playlistmaker.search.domain.model.Track

interface SongsRepository {
    fun searchSong(expression: String): List<Track>
}
