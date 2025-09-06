package com.example.playlistmaker.search.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.search.domain.HistoryInteractor
import com.example.playlistmaker.search.domain.SongInteractor
import com.example.playlistmaker.search.domain.model.SearchScreenState
import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class SearchViewModel(
    private val songInteractor: SongInteractor,
    private val historyInteractor: HistoryInteractor
) : ViewModel() {
    private val screenState: MutableLiveData<SearchScreenState> = MutableLiveData()
    private var searchText = ""
    private var searchJob: Job? = null
    fun getScreenState(): LiveData<SearchScreenState> = screenState

    init {
        setState(SearchScreenState.Nothing)
    }

    fun setState(state: SearchScreenState) {
        screenState.postValue(state)
    }

    fun searchDebounce(text: String) {
        if (text == searchText) return

        searchText = text

        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY)
            searchSong()
        }
    }

    private fun searchSong() {
        screenState.postValue(SearchScreenState.InProgress)
        viewModelScope.launch {
            songInteractor.searchSong(searchText)
                .catch {
                    screenState.postValue(SearchScreenState.ErrorSearch)
                }.collect {
                    if (it.isNotEmpty()) {
                        screenState.postValue(SearchScreenState.SuccessSearch(it))
                    } else {
                        screenState.postValue(SearchScreenState.EmptySearch)
                    }
                }
        }
    }

    fun addHistory(track: Track) {
        Log.d("TEST", "addHistory")
        historyInteractor.putTrackToHistory(track)
    }

    fun clearHistory() {
        historyInteractor.clearTrackHistory()
        screenState.postValue(SearchScreenState.Nothing)
    }

    fun getHistory(): ArrayList<Track> {
        return historyInteractor.getTrackHistory()
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 1000L
    }
}
