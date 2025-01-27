package com.example.playlistmaker.ui.settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsActivity : AppCompatActivity() {
    private var _binding: ActivitySettingsBinding? = null
    private val binding get() = _binding!!
    private lateinit var settingsViewModel: SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySettingsBinding.inflate(layoutInflater)
        settingsViewModel =
            ViewModelProvider(
                this,
                SettingsViewModelFactory.getViewModelFactory(this, application),
            )[SettingsViewModel::class.java]

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
