package com.example.playlistmaker.search.data

import com.example.playlistmaker.search.domain.model.Track

interface SongsRepository {
    fun searchSong(expression: String): ArrayList<Track>
}
