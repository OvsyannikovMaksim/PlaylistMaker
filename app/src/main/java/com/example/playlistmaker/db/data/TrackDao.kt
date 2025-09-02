package com.example.playlistmaker.db.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TrackDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: TrackEntity)

    @Delete
    suspend fun deleteTrack(track: TrackEntity)

    @Query("SELECT * FROM track_table")
    suspend fun getAllTracks(): List<TrackEntity>

    @Query("SELECT COUNT(*) FROM track_table WHERE trackName=:trackName AND artistName=:artistName")
    suspend fun isTrackInFavourite(trackName: String, artistName: String): Int
}