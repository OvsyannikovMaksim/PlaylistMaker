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
    private val historyList: MutableLiveData<ArrayList<Track>> = MutableLiveData()
    private val searchList: MutableLiveData<ArrayList<Track>> = MutableLiveData()

    fun getScreenState(): LiveData<SearchScreenState> = screenState

    fun getHistoryList(): LiveData<ArrayList<Track>> = historyList

    fun getSearchList(): LiveData<ArrayList<Track>> = searchList

    init {
        historyList.value = SharedPreferences.getTrackHistory(application.applicationContext)
        if (historyList.value.isNullOrEmpty()) {
            setState(SearchScreenState.Nothing)
        } else {
            setState(SearchScreenState.History)
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
                        searchList.postValue(foundSongs)
                        screenState.postValue(SearchScreenState.SuccessSearch)
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
        historyList.postValue(SharedPreferences.getTrackHistory(application.applicationContext))
    }

    fun clearHistory() {
        SharedPreferences.clearTrackHistory(application.applicationContext)
        historyList.postValue(arrayListOf())
        screenState.postValue(SearchScreenState.Nothing)
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 1000L
    }
}
