package com.example.playlistmaker.search.ui

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.utils.Creator

object SearchViewModelFactory {
    fun getViewModelFactory(context: Context): ViewModelProvider.Factory =
        viewModelFactory {
            initializer {
                SearchViewModel(Creator.getSongInteractor(), Creator.getHistoryInteractor(context))
            }
        }
}
