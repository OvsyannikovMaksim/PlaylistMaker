package com.example.playlistmaker.search.data.dto

data class SongResponse(
    val resultCount: Int,
    val results: List<TrackDto>,
) : Response()
