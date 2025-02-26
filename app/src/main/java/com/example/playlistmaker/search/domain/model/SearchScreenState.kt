package com.example.playlistmaker.search.domain.model

sealed class SearchScreenState {
    data object InProgress : SearchScreenState()

    data class SuccessSearch(val tracks: ArrayList<Track>) : SearchScreenState()

    data class History(val tracks: ArrayList<Track>) : SearchScreenState()

    data object EmptySearch : SearchScreenState()

    data object ErrorSearch : SearchScreenState()

    data object Nothing : SearchScreenState()
}
