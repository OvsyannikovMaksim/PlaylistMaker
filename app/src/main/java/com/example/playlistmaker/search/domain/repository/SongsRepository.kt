package com.example.playlistmaker.search.domain.repository

import com.example.playlistmaker.search.domain.model.Track

interface SongsRepository {
    fun searchSong(expression: String): ArrayList<Track>
}
