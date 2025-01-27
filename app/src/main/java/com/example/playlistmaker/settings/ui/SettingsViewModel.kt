package com.example.playlistmaker.settings.ui

import androidx.lifecycle.ViewModel
import com.example.playlistmaker.settings.domain.SettingsInteractor
import com.example.playlistmaker.sharing.domain.SharingInteractor

class SettingsViewModel(
    private val sharingInteractor: SharingInteractor,
    private val settingsInteractor: SettingsInteractor,
) : ViewModel() {
    val isDarkTheme = settingsInteractor.getThemeSettings()

    fun startShareIntent() {
        sharingInteractor.shareApp()
    }

    fun startSupportIntent() {
        sharingInteractor.openSupport()
    }

    fun startEulaIntent() {
        sharingInteractor.openTerms()
    }

    fun changeThemeMode(checked: Boolean) {
        settingsInteractor.updateThemeSetting(checked)
    }
}
