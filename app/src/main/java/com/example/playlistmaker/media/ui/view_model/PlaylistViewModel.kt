package com.example.playlistmaker.media.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.media.domain.PlaylistInteractor
import com.example.playlistmaker.media.domain.model.Playlist
import com.example.playlistmaker.media.domain.model.PlaylistScreenState
import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.launch

class PlaylistViewModel(private val playlistInteractor: PlaylistInteractor) : ViewModel() {

    private val state: MutableLiveData<PlaylistScreenState> = MutableLiveData()
    fun getState(): LiveData<PlaylistScreenState> = state

    fun getState(playlistId: Int) {
        viewModelScope.launch {
            var playlist: Playlist? = null
            var tracks: List<Track> = emptyList()
            playlistInteractor.getPlaylistInfo(playlistId).collect { playlist = it }
            playlistInteractor.getTracksInPlaylist(playlistId).collect { tracks = it.reversed() }
            val minutes = playlistInteractor.getTimeOfTrackInPlaylist(playlistId)
            state.postValue(PlaylistScreenState(playlist!!, tracks, minutes))
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
            getState(playlist.id)
        }
    }

    fun deletePlaylist() {
        viewModelScope.launch {
            playlistInteractor.deletePlaylist(state.value?.playlist?.id ?: 0)
        }
    }

    fun share(shareClick: ShareClick) {
        if (state.value?.tracks.isNullOrEmpty()) {
            shareClick.onEmptyList()
        } else {
            shareClick.onList(createShareContent())
        }
    }

    private fun createShareContent(): String {
        val stringBuilder = StringBuilder()
            .appendLine(state.value?.playlist?.name)
            .appendLine(state.value?.playlist?.desc)
            .appendLine("${state.value?.playlist?.tracksAmount} треков")
        state.value?.tracks?.forEachIndexed { index, track ->
            stringBuilder.appendLine("${index + 1}. ${track.artistName} - ${track.trackName} (${track.trackTime})")
        }
        return stringBuilder.toString()
    }

    interface ShareClick {
        fun onEmptyList()
        fun onList(string: String)
    }
}
