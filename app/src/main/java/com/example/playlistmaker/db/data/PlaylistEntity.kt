package com.example.playlistmaker.db.data;

import android.net.Uri
import androidx.room.Entity;
import androidx.room.PrimaryKey

@Entity(tableName = "playlist_table")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val desc: String,
    val imagePath: String?,
    val tracksAmount: Int
)
