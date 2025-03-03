package com.example.playlistmaker

import android.app.Application
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.di.dataModule
import com.example.playlistmaker.di.domainModule
import com.example.playlistmaker.di.viewModelModule
import com.example.playlistmaker.main.data.SharedPreferences
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    private val sharedPreferences: SharedPreferences by inject()

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(dataModule, domainModule, viewModelModule)
        }

        if (sharedPreferences.isFirstRun()) {
            sharedPreferences.putIsFirstRun(false)
            val currentNightMode =
                resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
            when (currentNightMode) {
                Configuration.UI_MODE_NIGHT_YES -> {
                    sharedPreferences.putNightMode( true)
                }

                Configuration.UI_MODE_NIGHT_NO -> {
                    sharedPreferences.putNightMode( false)
                }
            }
        }
        switchTheme(sharedPreferences.getNightMode())
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            },
        )
    }
}
