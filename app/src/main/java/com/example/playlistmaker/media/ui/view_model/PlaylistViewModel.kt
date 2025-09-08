package com.example.playlistmaker.media.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.media.domain.PlaylistInteractor
import com.example.playlistmaker.media.domain.model.Playlist
import kotlinx.coroutines.launch

class PlaylistViewModel(private val playlistInteractor: PlaylistInteractor): ViewModel() {

    private val playlists: MutableLiveData<List<Playlist>> = MutableLiveData()
    fun playlists(): LiveData<List<Playlist>> = playlists

    fun getPlaylists() {
        viewModelScope.launch {
            playlistInteractor.getPlaylists().collect{
                playlists.postValue(it)
            }
        }
    }
}