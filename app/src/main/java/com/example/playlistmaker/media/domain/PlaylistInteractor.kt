package com.example.playlistmaker.media.domain

import com.example.playlistmaker.media.domain.model.Playlist

interface PlaylistInteractor {

    suspend fun addPlayList(playlist: Playlist)
    suspend fun removePlayList(playlist: Playlist)
}