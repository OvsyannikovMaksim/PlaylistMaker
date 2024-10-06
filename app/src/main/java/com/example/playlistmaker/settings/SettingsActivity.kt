package com.example.playlistmaker.settings

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.R
import com.example.playlistmaker.utils.SharedPreferences
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsActivity : AppCompatActivity() {
    private lateinit var settingsViewModel: SettingsViewModel
    private lateinit var darkThemeSwitch: SwitchMaterial
    private lateinit var shareButton: TextView
    private lateinit var supportButton: TextView
    private lateinit var eulaButton: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val toolbar = findViewById<Toolbar>(R.id.settings_toolbar)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        settingsViewModel = ViewModelProvider(this).get(SettingsViewModel::class.java)

        darkThemeSwitch =
            findViewById<SwitchMaterial>(R.id.dark_theme_switch).apply {
                isChecked = SharedPreferences.getNightMode(context)
            }
        shareButton = findViewById(R.id.share_button)
        supportButton = findViewById(R.id.support_button)
        eulaButton = findViewById(R.id.eula_button)

        shareButton.setOnClickListener {
            settingsViewModel.startShareIntent()
        }

        supportButton.setOnClickListener {
            settingsViewModel.startSupportIntent()
        }

        eulaButton.setOnClickListener {
            settingsViewModel.startEulaIntent()
        }

        darkThemeSwitch.setOnCheckedChangeListener { _, checked ->
            settingsViewModel.changeThemeMode(checked)
        }
    }
}
