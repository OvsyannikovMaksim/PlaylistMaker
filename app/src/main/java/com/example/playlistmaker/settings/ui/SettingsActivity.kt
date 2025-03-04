package com.example.playlistmaker.settings.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : AppCompatActivity() {
    private var _binding: ActivitySettingsBinding? = null
    private val binding get() = _binding!!
    private val settingsViewModel by viewModel<SettingsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySettingsBinding.inflate(layoutInflater)

        setContentView(binding.root)
        setSupportActionBar(binding.settingsToolbar)
        binding.settingsToolbar.setNavigationOnClickListener {
            finish()
        }
        binding.darkThemeSwitch.isChecked = settingsViewModel.isDarkTheme

        binding.darkThemeSwitch.setOnCheckedChangeListener { _, checked ->
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
