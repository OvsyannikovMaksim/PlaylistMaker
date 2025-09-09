package com.example.playlistmaker.media.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.media.domain.PlaylistInteractor
import com.example.playlistmaker.media.domain.model.Playlist
import kotlinx.coroutines.launch

class ChangePlaylistViewModel(private val playlistInteractor: PlaylistInteractor) : ViewModel() {

    private val playlist: MutableLiveData<Playlist> = MutableLiveData()
    fun getPlaylist(): LiveData<Playlist> = playlist

    fun getPlaylist(playlistId: Int) {
        viewModelScope.launch {
            playlistInteractor.getPlaylistInfo(playlistId).collect{
                playlist.postValue(it)
            }
        }
    }

    fun savePlayList(playlistName: String, desc: String, imagePath: String?) {
        val newPlaylist = Playlist(
            playlist.value?.id ?: 0,
            playlistName,
            desc,
            imagePath,
            playlist.value?.tracksAmount ?: 0
        )
        viewModelScope.launch {
            playlistInteractor.addPlayList(newPlaylist)
        }
    }

}