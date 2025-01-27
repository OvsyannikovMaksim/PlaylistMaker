package com.example.playlistmaker.ui.search

import com.example.playlistmaker.domain.search.model.Track

sealed class SearchScreenState {
    data class Success(val trackList: List<Track>):SearchScreenState()
    data object EmptyResult: SearchScreenState()
    data object Error: SearchScreenState()
}