package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.Track

interface SongInteractor {
    fun searchSong(
        expression: String,
        consumer: SongConsumer,
    )

    interface SongConsumer  {
        fun onSuccess(foundSongs: List<Track>)

        fun onFailure(exception: Exception)
    }

    interface Song
}
