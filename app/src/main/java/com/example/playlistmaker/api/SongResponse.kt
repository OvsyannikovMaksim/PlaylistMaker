package com.example.playlistmaker.api

data class SongResponse(
    val resultCount: Int,
    val results: List<Track>,
)
