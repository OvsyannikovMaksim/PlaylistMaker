package com.example.playlistmaker.media.domain.repository

import com.example.playlistmaker.media.domain.model.Playlist

interface PlaylistRepository {

    suspend fun addPlayList(playlist: Playlist)
    suspend fun removePlayList(playlist: Playlist)
}