package com.example.playlistmaker.ui.settings

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    private lateinit var settingsViewModel: SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_settings)
        setSupportActionBar(binding.settingsToolbar)
        binding.settingsToolbar.setNavigationOnClickListener {
            finish()
        }

        settingsViewModel =
            ViewModelProvider(
                this,
                SettingsViewModelFactory.getViewModelFactory(this, application),
            )[SettingsViewModel::class.java]

        binding.darkThemeSwitch.isChecked = settingsViewModel.isDarkTheme

        binding.darkThemeSwitch.setOnCheckedChangeListener { _, checked ->
            Log.d("TEST", "darkThemeSwitch")
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
