package com.example.playlistmaker.ui.settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private lateinit var settingsViewModel: SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        settingsViewModel =
            ViewModelProvider(
                this,
                SettingsViewModelFactory.getViewModelFactory(this, application),
            )[SettingsViewModel::class.java]

        setContentView(R.layout.activity_settings)
        setSupportActionBar(binding.settingsToolbar)
        binding.settingsToolbar.setNavigationOnClickListener {
            finish()
        }
        val darkThemeSwitch = findViewById<SwitchMaterial>(R.id.dark_theme_switch)
        darkThemeSwitch.isChecked = settingsViewModel.isDarkTheme

        darkThemeSwitch.setOnCheckedChangeListener { _, checked ->
            settingsViewModel.changeThemeMode(checked)
        }

        binding.shareButton.setOnClickListener {
            settingsViewModel.startShareIntent()
        }

        binding.supportButton.setOnClickListener {
            settingsViewModel.startSupportIntent()
        }

        binding.eulaButton.setOnClickListener {
            settingsViewModel.startEulaIntent()
        }
    }
}
