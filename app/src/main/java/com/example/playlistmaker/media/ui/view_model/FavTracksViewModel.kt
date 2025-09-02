package com.example.playlistmaker.media.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.media.domain.FavTracksInteractor
import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.launch

class FavTracksViewModel(val favTracksInteractor: FavTracksInteractor) : ViewModel() {

    private val favTracks: MutableLiveData<List<Track>> = MutableLiveData()
    fun favTracks(): LiveData<List<Track>> = favTracks

   fun getFavTracks() {
        viewModelScope.launch {
            favTracksInteractor.getFavTracks().collect {
                favTracks.postValue(it)
            }
        }
    }
}