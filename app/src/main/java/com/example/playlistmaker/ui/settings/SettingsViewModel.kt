package com.example.playlistmaker.ui.settings

import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.settings.SettingsInteractor
import com.example.playlistmaker.domain.sharing.SharingInteractor

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
