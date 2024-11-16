package com.example.playlistmaker.audioplayer

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat.getDrawable
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.api.Track
import com.example.playlistmaker.utils.Utils
import com.example.playlistmaker.utils.Utils.dpToPx

class AudioPlayerActivity : AppCompatActivity() {

    private val handler = Handler(Looper.getMainLooper())
    private var playerState = PlayerState.Default
    private var mediaPlayer = MediaPlayer()
    private var trackInfo: Track? = null
    private lateinit var poster: ImageView
    private lateinit var trackName: TextView
    private lateinit var artistName: TextView
    private lateinit var trackTime: TextView
    private lateinit var album: TextView
    private lateinit var albumTitle: TextView
    private lateinit var year: TextView
    private lateinit var genre: TextView
    private lateinit var country: TextView
    private lateinit var playBtn: ImageButton
    private lateinit var currentTime: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        trackInfo = getTrack(intent)
        setContentView(R.layout.activity_audioplayer)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        poster = findViewById(R.id.poster)
        trackName = findViewById(R.id.track_name)
        artistName = findViewById(R.id.artist_name)
        trackTime = findViewById(R.id.track_time)
        genre = findViewById(R.id.genre)
        year = findViewById(R.id.year)
        album = findViewById(R.id.album)
        albumTitle = findViewById(R.id.album_title)
        country = findViewById(R.id.country)
        playBtn = findViewById(R.id.play_button)
        currentTime = findViewById(R.id.current_time)

        setTrack()
        prepareMediaPlayer()
        playBtn.setOnClickListener {
            when (playerState) {
                PlayerState.Paused, PlayerState.Prepared -> toPlayingState()
                PlayerState.Playing -> toPauseState()
                PlayerState.Default -> null
            }
        }
    }

    override fun onPause() {
        super.onPause()
        toPauseState()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
        handler.removeCallbacks(updateTimer)
    }

    private fun toPauseState()  {
        mediaPlayer.pause()
        playBtn.background = getDrawable(this, R.drawable.play_button)
        playerState = PlayerState.Paused
        handler.removeCallbacks(updateTimer)
    }

    private fun toPlayingState()  {
        mediaPlayer.start()
        playBtn.background = getDrawable(this, R.drawable.pause_button)
        playerState = PlayerState.Playing
        handler.postDelayed(updateTimer, REFRESH_TIMER_DELAY_MILLIS)
    }

    private fun getTrack(intent: Intent): Track? =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra(TRACK_INFO, Track::class.java)
        } else {
            intent.getSerializableExtra(TRACK_INFO) as Track
        }

    private fun setTrack() {
        Glide
            .with(this)
            .load(trackInfo?.artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg"))
            .placeholder(R.drawable.placeholder)
            .fitCenter()
            .transform(RoundedCorners(dpToPx(8.0F, this)))
            .into(poster)
        trackName.text = trackInfo?.trackName
        artistName.text = trackInfo?.artistName
        genre.text = trackInfo?.primaryGenreName
        year.text = trackInfo?.releaseDate?.slice(0..3)
        if (trackInfo?.collectionName == null) {
            album.isVisible = false
            albumTitle.isVisible = false
        } else {
            album.isVisible = true
            albumTitle.isVisible = true
        }
        album.text = trackInfo?.collectionName
        country.text = trackInfo?.country
        trackTime.text = Utils.timeConverter(trackInfo?.trackTimeMillis)
    }

    private fun prepareMediaPlayer() {
        mediaPlayer.setDataSource(trackInfo?.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState = PlayerState.Prepared
        }
        mediaPlayer.setOnCompletionListener {
            playerState = PlayerState.Prepared
            handler.removeCallbacks(updateTimer)
            currentTime.text = getString(R.string.default_current_time)
            playBtn.background = getDrawable(this, R.drawable.play_button)
        }
    }

    private val updateTimer =
        object : Runnable {
            override fun run() {
                currentTime.text = Utils.timeConverter(mediaPlayer.currentPosition.toLong())
                handler.postDelayed(this, REFRESH_TIMER_DELAY_MILLIS)
            }
        }

    enum class PlayerState(
        val id: Int,
    ) {
        Default(0),
        Prepared(1),
        Playing(2),
        Paused(3),
    }

    companion object {
        private const val TRACK_INFO = "TRACK_INFO"
        private const val REFRESH_TIMER_DELAY_MILLIS = 500L

        fun launch(
            context: Context,
            track: Track,
        ) = context.startActivity(
            Intent(context, AudioPlayerActivity::class.java)
                .putExtra(TRACK_INFO, track),
        )
    }
}
