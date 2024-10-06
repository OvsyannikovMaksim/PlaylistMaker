package com.example.playlistmaker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.playlistmaker.utils.SharedPreferences
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsActivity : AppCompatActivity() {
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

        darkThemeSwitch =
            findViewById<SwitchMaterial>(R.id.dark_theme_switch).apply {
                isChecked = SharedPreferences.getNightMode(context)
            }
        shareButton = findViewById(R.id.share_button)
        supportButton = findViewById(R.id.support_button)
        eulaButton = findViewById(R.id.eula_button)

        shareButton.setOnClickListener {
            startShareIntent()
        }

        supportButton.setOnClickListener {
            startSupportIntent()
        }

        eulaButton.setOnClickListener {
            startEulaIntent()
        }

        darkThemeSwitch.setOnCheckedChangeListener { _, checked ->
            (applicationContext as App).switchTheme(checked)
            SharedPreferences.putNightMode(this, checked)
        }
    }

    private fun startShareIntent() {
        val intent = Intent(Intent.ACTION_SEND)
        intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.practikum_link))
        intent.setType("text/plain")
        startActivity(Intent.createChooser(intent, getString(R.string.chooser_text)))
    }

    private fun startSupportIntent() {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:")
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.email_to)))
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject))
        intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.email_body))
        startActivity(intent)
    }

    private fun startEulaIntent() {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setData(Uri.parse(getString(R.string.eula_link)))
        startActivity(intent)
    }
}
