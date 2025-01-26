package com.example.playlistmaker.domain.settings.impl

import com.example.playlistmaker.data.settings.SettingsRepository
import com.example.playlistmaker.domain.settings.SettingsInteractor

class SettingsInteractorImpl(
    private val settingsRepository: SettingsRepository,
) : SettingsInteractor {
    override fun getThemeSettings(): Boolean = settingsRepository.getThemeSettings()

    override fun updateThemeSetting(isDarkTheme: Boolean) {
        settingsRepository.updateThemeSetting(isDarkTheme)
    }
}
