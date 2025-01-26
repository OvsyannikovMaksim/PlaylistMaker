package com.example.playlistmaker.ui.settings

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.utils.Creator

object SettingsViewModelFactory {
    fun getViewModelFactory(
        context: Context,
        application: Application,
    ): ViewModelProvider.Factory =
        viewModelFactory {
            initializer {
                SettingsViewModel(
                    Creator.getSharingInteractor(context),
                    Creator.getSettingsInteractor(context, application),
                )
            }
        }
}
