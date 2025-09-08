package com.example.playlistmaker.db.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist_to_track")
data class PlaylistToTrackEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val playlistId: Int,
    val trackId: Int
)