package com.example.playlistmaker.db.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy

@Dao
interface PlaylistDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(track: PlaylistEntity)

    @Delete
    suspend fun deletePlaylist(track: PlaylistEntity)
}