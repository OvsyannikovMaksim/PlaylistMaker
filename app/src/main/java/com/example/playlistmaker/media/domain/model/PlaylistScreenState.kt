package com.example.playlistmaker.media.domain.model

import com.example.playlistmaker.search.domain.model.Track

data class PlaylistScreenState(val playlist: Playlist, val tracks: List<Track>, val sumTimeImMin: Int)
