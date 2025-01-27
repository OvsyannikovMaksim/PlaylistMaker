package com.example.playlistmaker.search.ui

import com.example.playlistmaker.search.domain.model.Track

sealed class SearchScreenState {
    data class Success(val trackList: List<Track>): SearchScreenState()
    data object EmptyResult: SearchScreenState()
    data object Error: SearchScreenState()
}