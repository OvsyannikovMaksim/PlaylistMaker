package com.example.playlistmaker

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val clickListener: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                Toast.makeText(this@MainActivity, "Tap on Settings Button", Toast.LENGTH_SHORT).show()
            }
        }
        val searchButton = findViewById<Button>(R.id.search_button)
        val mediaButton = findViewById<Button>(R.id.media_button)
        val settingsButton = findViewById<Button>(R.id.settings_button)

        searchButton.setOnClickListener{
            Toast.makeText(this, "Tap on Search Button", Toast.LENGTH_SHORT).show()
        }
        mediaButton.setOnClickListener{
            Toast.makeText(this, "Tap on Media Button", Toast.LENGTH_SHORT).show()
        }
        settingsButton.setOnClickListener(clickListener)
    }
}