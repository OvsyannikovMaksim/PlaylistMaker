package com.example.playlistmaker.db.data;

import android.net.Uri
import androidx.room.Entity;
import androidx.room.PrimaryKey

@Entity(tableName = "playlist_table")
data class PlaylistEntity(
    val name: String,
    val desc: String,
    val image: String,
    val tracks: String,
    val tracksAmount: Int
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}
