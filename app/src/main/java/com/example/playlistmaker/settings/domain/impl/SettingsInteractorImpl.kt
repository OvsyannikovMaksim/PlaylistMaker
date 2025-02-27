package com.example.playlistmaker.settings.domain.impl

import com.example.playlistmaker.settings.domain.repository.SettingsRepository
import com.example.playlistmaker.settings.domain.SettingsInteractor

class SettingsInteractorImpl(
    private val settingsRepository: SettingsRepository,
) : SettingsInteractor {
    override fun getThemeSettings(): Boolean = settingsRepository.getThemeSettings()

    override fun updateThemeSetting(isDarkTheme: Boolean) {
        settingsRepository.updateThemeSetting(isDarkTheme)
    }
}
