package com.example.playlistmaker.search.ui

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.utils.Creator

object SearchViewModelFactory {
    fun getViewModelFactory(application: Application): ViewModelProvider.Factory =
        viewModelFactory {
            initializer {
                SearchViewModel(Creator.getSongInteractor(), application)
            }
        }
}
