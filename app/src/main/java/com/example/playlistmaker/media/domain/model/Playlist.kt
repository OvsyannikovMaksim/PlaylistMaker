package com.example.playlistmaker.media.domain.model

import android.net.Uri
import com.example.playlistmaker.search.domain.model.Track

data class Playlist(val name: String, val desc: String, val imagePath: String?, val tracks: List<Track>, val tracksAmount: Int)