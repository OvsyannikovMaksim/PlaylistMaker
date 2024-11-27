package com.example.playlistmaker.data.dto

data class SongResponse(
    val resultCount: Int,
    val results: List<TrackDto>,
) : Response()
