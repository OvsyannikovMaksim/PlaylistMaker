package com.example.playlistmaker.search.domain.model

sealed class SearchScreenState {
    data object InProgress : SearchScreenState()

    data object SuccessSearch : SearchScreenState()

    data object History : SearchScreenState()

    data object EmptySearch : SearchScreenState()

    data object ErrorSearch : SearchScreenState()

    data object Nothing : SearchScreenState()
}
