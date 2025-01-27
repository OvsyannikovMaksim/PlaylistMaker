package com.example.playlistmaker.search.ui


import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.utils.Creator

object SearchViewModelFactory {
    fun getViewModelFactory(
    ): ViewModelProvider.Factory {
        return viewModelFactory {
            initializer {
                SearchViewModel(
                    Creator.getSongInteractor()
                )
            }
        }
    }
}