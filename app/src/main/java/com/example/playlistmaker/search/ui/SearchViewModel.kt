package com.example.playlistmaker.search.ui

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.search.domain.HistoryInteractor
import com.example.playlistmaker.search.domain.SongInteractor
import com.example.playlistmaker.search.domain.model.SearchScreenState
import com.example.playlistmaker.search.domain.model.Track

class SearchViewModel(
    private val songInteractor: SongInteractor,
    private val historyInteractor: HistoryInteractor
) : ViewModel() {
    private val handler = Handler(Looper.getMainLooper())
    private val screenState: MutableLiveData<SearchScreenState> = MutableLiveData()
    private var searchText = ""
    private val searchRunnable = Runnable { searchSong() }
    fun getScreenState(): LiveData<SearchScreenState> = screenState

    init {
        val history = getHistory()
        if (history.isEmpty()) {
            setState(SearchScreenState.Nothing)
        } else {
            setState(SearchScreenState.History(history))
        }
    }

    fun setState(state: SearchScreenState) {
        screenState.postValue(state)
    }

    fun searchDebounce(text: String) {
        searchText = text
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    private fun searchSong() {
        screenState.postValue(SearchScreenState.InProgress)
        songInteractor.searchSong(
            searchText,
            object : SongInteractor.SongConsumer {
                override fun onSuccess(foundSongs: ArrayList<Track>) {
                    if (foundSongs.isNotEmpty()) {
                        screenState.postValue(SearchScreenState.SuccessSearch(foundSongs))
                    } else {
                        screenState.postValue(SearchScreenState.EmptySearch)
                    }
                }

                override fun onFailure(exception: Exception) {
                    screenState.postValue(SearchScreenState.ErrorSearch)
                }
            },
        )
    }

    fun addHistory(track: Track) {
        historyInteractor.putTrackToHistory(track)
    }

    fun clearHistory() {
        historyInteractor.clearTrackHistory()
        screenState.postValue(SearchScreenState.Nothing)
    }

    fun getHistory(): ArrayList<Track>{
        return historyInteractor.getTrackHistory()
    }

    fun removeCallbacks() {
        searchText = ""
        handler.removeCallbacks(searchRunnable)
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 1000L
    }
}
