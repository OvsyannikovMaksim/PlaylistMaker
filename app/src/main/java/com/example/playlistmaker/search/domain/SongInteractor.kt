package com.example.playlistmaker.search.domain

import com.example.playlistmaker.search.domain.model.Track

interface SongInteractor {
    fun searchSong(
        expression: String,
        consumer: SongConsumer,
    )

    interface SongConsumer {
        fun onSuccess(foundSongs: ArrayList<Track>)

        fun onFailure(exception: Exception)
    }
}
