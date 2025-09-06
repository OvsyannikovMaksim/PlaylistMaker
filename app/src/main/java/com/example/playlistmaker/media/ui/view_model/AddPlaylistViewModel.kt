package com.example.playlistmaker.media.ui.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.media.domain.PlaylistInteractor
import com.example.playlistmaker.media.domain.model.Playlist
import kotlinx.coroutines.launch

class AddPlaylistViewModel(val playlistInteractor: PlaylistInteractor): ViewModel() {

    fun savePlayList(playlist: Playlist) {
        viewModelScope.launch {
            playlistInteractor.addPlayList(playlist)
        }
    }
}