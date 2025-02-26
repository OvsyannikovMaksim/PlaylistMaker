package com.example.playlistmaker.search.ui

import android.app.Application
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.playlistmaker.main.data.SharedPreferences
import com.example.playlistmaker.search.domain.SongInteractor
import com.example.playlistmaker.search.domain.model.SearchScreenState
import com.example.playlistmaker.search.domain.model.Track

class SearchViewModel(
    private val songInteractor: SongInteractor,
    private val application: Application,
) : AndroidViewModel(application) {
    private val handler = Handler(Looper.getMainLooper())
    private val screenState: MutableLiveData<SearchScreenState> = MutableLiveData()
    fun getScreenState(): LiveData<SearchScreenState> = screenState

    init {
        val history = SharedPreferences.getTrackHistory(application.applicationContext)
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
        val searchRunnable =
            Runnable { searchSong(text) }
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    private fun searchSong(text: String) {
        screenState.postValue(SearchScreenState.InProgress)
        songInteractor.searchSong(
            text,
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
        SharedPreferences.putTrackToHistory(application.applicationContext, track)
    }

    fun clearHistory() {
        SharedPreferences.clearTrackHistory(application.applicationContext)
        screenState.postValue(SearchScreenState.Nothing)
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 1000L
    }
}
