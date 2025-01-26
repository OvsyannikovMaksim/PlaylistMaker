package com.example.playlistmaker.data.settings.impl

import android.app.Application
import android.content.Context
import android.util.Log
import com.example.playlistmaker.App
import com.example.playlistmaker.data.SharedPreferences
import com.example.playlistmaker.data.settings.SettingsRepository

class SettingsRepositoryImpl(
    private val context: Context,
    private val application: Application,
) : SettingsRepository {
    override fun getThemeSettings(): Boolean = SharedPreferences.getNightMode(context)

    override fun updateThemeSetting(isDarkTheme: Boolean) {
        Log.d("TEST", isDarkTheme.toString())
        (application as App).switchTheme(isDarkTheme)
        SharedPreferences.putNightMode(context, isDarkTheme)
    }
}
