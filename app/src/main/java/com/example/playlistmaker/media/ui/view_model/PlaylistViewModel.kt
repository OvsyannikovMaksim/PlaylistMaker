package com.example.playlistmaker.media.ui.view_model

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.di.viewModelModule
import com.example.playlistmaker.media.domain.PlaylistInteractor
import com.example.playlistmaker.media.domain.model.Playlist
import com.example.playlistmaker.media.domain.model.PlaylistScreenState
import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.launch

class PlaylistViewModel(private val playlistInteractor: PlaylistInteractor) : ViewModel() {

    private val state: MutableLiveData<PlaylistScreenState> = MutableLiveData()
    fun getState(): LiveData<PlaylistScreenState> = state

    fun getState(playlistId: Int) {
        Log.d("TEST", "getState")
        viewModelScope.launch {
            var playlist: Playlist? = null
            var tracks: List<Track> = emptyList()
            playlistInteractor.getPlaylistInfo(playlistId).collect { playlist = it }
            playlistInteractor.getTracksInPlaylist(playlistId).collect { tracks = it }
            state.postValue(PlaylistScreenState(playlist!!, tracks))
        }
    }

    fun deleteTrackFromPlaylist(playlist: Playlist, track: Track) {
        viewModelScope.launch {
            playlistInteractor.deleteTrack(playlist.id, track)
            playlistInteractor.addPlayList(
                Playlist(
                    playlist.id,
                    playlist.name,
                    playlist.desc,
                    playlist.imagePath,
                    playlist.tracksAmount - 1
                )
            )
        }
        getState(playlist.id)
    }

    fun share() {
        if(state.value?.tracks.isNullOrEmpty()){
            Toast.makeText(viewModelModule, "", Toast.LENGTH_SHORT)
        }
    }
}