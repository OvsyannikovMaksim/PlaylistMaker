package com.example.playlistmaker.search.ui


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.search.domain.SongInteractor
import com.example.playlistmaker.search.domain.model.Track

class SearchViewModel(private val songInteractor: SongInteractor) : ViewModel() {

    private val screenState: MutableLiveData<SearchScreenState> = MutableLiveData()
    private val historyList: MutableLiveData<ArrayList<Track>> = MutableLiveData()
    private val searchList: MutableLiveData<ArrayList<Track>> = MutableLiveData()

    fun getScreenState(): LiveData<SearchScreenState> = screenState
    fun getHistoryList(): LiveData<ArrayList<Track>> = historyList
    fun getSearchList(): LiveData<ArrayList<Track>> = searchList

//    init {
//        historyList.value = SharedPreferences.getTrackHistory(context)
//    }

    fun searchSong(text: String) {
        songInteractor.searchSong(text, object : SongInteractor.SongConsumer {
            override fun onSuccess(foundSongs: List<Track>) {
                if (foundSongs.isNotEmpty()) {
                    screenState.postValue(SearchScreenState.Success(foundSongs))
                } else {
                    screenState.postValue(SearchScreenState.EmptyResult)
                }
            }

            override fun onFailure(exception: Exception) {
                screenState.postValue(SearchScreenState.Error)
            }
        })
    }

    fun searchClear(){

    }
}