package com.example.playlistmaker.domain.search

import com.example.playlistmaker.domain.search.model.Track

interface SongInteractor {
    fun searchSong(
        expression: String,
        consumer: SongConsumer,
    )

    interface SongConsumer {
        fun onSuccess(foundSongs: List<Track>)

        fun onFailure(exception: Exception)
    }
}
