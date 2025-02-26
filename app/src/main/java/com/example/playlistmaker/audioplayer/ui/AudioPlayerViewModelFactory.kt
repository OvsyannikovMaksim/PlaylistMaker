package com.example.playlistmaker.audioplayer.ui

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.search.ui.SearchViewModel
import com.example.playlistmaker.utils.Creator

object AudioPlayerViewModelFactory {

    fun getViewModelFactory(application: Application): ViewModelProvider.Factory =
        viewModelFactory {
            initializer {
                AudioPlayerViewModel(application, Creator.getMediaPlayerInteractor())
            }
        }
}