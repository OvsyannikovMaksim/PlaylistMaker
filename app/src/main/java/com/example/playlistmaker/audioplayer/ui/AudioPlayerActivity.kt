package com.example.playlistmaker.audioplayer.ui

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getDrawable
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.audioplayer.domain.model.PlayerState
import com.example.playlistmaker.audioplayer.domain.model.ScreenState
import com.example.playlistmaker.databinding.ActivityAudioplayerBinding
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.utils.Utils.dpToPx

class AudioPlayerActivity : AppCompatActivity() {
    private var _binding: ActivityAudioplayerBinding? = null
    private val binding get() = _binding!!
    private val viewModel by lazy {
        ViewModelProvider(
            this,
            AudioPlayerViewModelFactory.getViewModelFactory(application)
        )[AudioPlayerViewModel::class.java]
    }

    private lateinit var screenState: ScreenState
    private var trackInfo: Track? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAudioplayerBinding.inflate(layoutInflater)
        trackInfo = getTrack(intent)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        viewModel.getPlayerState().observe(this) {
            screenState = it
            binding.playButton.background =
                when (it.playerState) {
                    PlayerState.Paused, PlayerState.Default, PlayerState.Prepared ->
                        getDrawable(
                            this,
                            R.drawable.play_button,
                        )

                    PlayerState.Playing -> getDrawable(this, R.drawable.pause_button)
                }
            binding.currentTime.text = it.currentTime
        }

        setTrack()
        prepareMediaPlayer()
        binding.playButton.setOnClickListener {
            when (screenState.playerState) {
                PlayerState.Paused, PlayerState.Prepared -> viewModel.startPlayer()
                PlayerState.Playing -> viewModel.pausePlayer()
                PlayerState.Default -> Unit
            }
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.releasePlayer()
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
            .into(binding.poster)
        with(binding) {
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
            binding.album.text = trackInfo?.collectionName
            binding.country.text = trackInfo?.country
            binding.trackTime.text = trackInfo?.trackTime
        }
    }

    private fun prepareMediaPlayer() {
        viewModel.prepareMediaPlayer(trackInfo?.previewUrl)
    }

    companion object {
        private const val TRACK_INFO = "TRACK_INFO"

        fun launch(
            context: Context,
            track: Track,
        ) = context.startActivity(
            Intent(context, AudioPlayerActivity::class.java)
                .putExtra(TRACK_INFO, track),
        )
    }
}
