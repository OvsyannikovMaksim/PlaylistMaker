package com.example.playlistmaker.db.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PlaylistDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(track: PlaylistEntity)

    @Delete
    suspend fun deletePlaylist(track: PlaylistEntity)

    @Query("SELECT * FROM playlist_table")
    suspend fun getPlaylists(): List<PlaylistEntity>

    @Query("SELECT id FROM tracks_in_fav WHERE artistName=:name AND trackName=:trackName")
    suspend fun getTrackId(name: String, trackName: String): Int?

    @Query("SELECT id FROM playlist_table WHERE name=:name")
    suspend fun getPlaylistId(name: String): Int

    @Query("SELECT COUNT(*) FROM playlist_to_track WHERE trackId=:trackId AND playlistId=:playlistId")
    suspend fun isTrackInPlaylist(playlistId: Int, trackId: Int): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTrack(track: TracksInPlaylistsEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTrackToPlaylist(playlistToTrackEntity: PlaylistToTrackEntity)
}