package com.example.playlistmaker.db.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PlaylistDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(track: PlaylistEntity)

    @Query("DELETE FROM tracks_in_playlist WHERE artistName=:artistName AND trackName=:trackName")
    suspend fun deleteTrack(artistName: String, trackName: String)

    @Query("DELETE FROM playlist_to_track WHERE trackId=:trackId AND playlistId=:playlistId")
    suspend fun deleteTrackFromPlaylistToTrack(playlistId: Int, trackId: Int)

    @Query("SELECT * FROM playlist_table")
    suspend fun getPlaylists(): List<PlaylistEntity>

    @Query("SELECT id FROM tracks_in_playlist WHERE artistName=:name AND trackName=:trackName")
    suspend fun getTrackId(name: String, trackName: String): Int?

    @Query("SELECT id FROM playlist_table WHERE name=:name")
    suspend fun getPlaylistId(name: String): Int

    @Query("SELECT COUNT(*) FROM playlist_to_track WHERE trackId=:trackId AND playlistId=:playlistId")
    suspend fun isTrackInPlaylist(playlistId: Int, trackId: Int): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTrack(track: TracksInPlaylistsEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTrackToPlaylist(playlistToTrackEntity: PlaylistToTrackEntity)

    @Query("SELECT tracks_in_playlist.* from tracks_in_playlist JOIN (SELECT * FROM playlist_to_track WHERE playlistId=:playlistId) as t1 ON tracks_in_playlist.id=t1.trackId")
    suspend fun getTrackListForPlaylist(playlistId: Int): List<TrackEntity>

    @Query("SELECT COUNT(*) from playlist_to_track WHERE trackId=:trackId")
    suspend fun isAnyPlaylistContainsTrack(trackId: Int): Int

    @Query("SELECT * FROM playlist_table WHERE id=:playlistId")
    suspend fun getPlaylistInfo(playlistId: Int): PlaylistEntity

    @Query("DELETE FROM playlist_table WHERE id=:playlistId")
    suspend fun deletePlaylist(playlistId: Int)

    @Query("DELETE FROM playlist_to_track WHERE playlistId=:playlistId")
    suspend fun deletePlaylistFromPlaylistToTrack(playlistId: Int)

    @Query("SELECT trackTime FROM tracks_in_playlist JOIN (SELECT * FROM playlist_to_track WHERE playlistId=:playlistId) as t1 ON tracks_in_playlist.id=t1.trackId")
    suspend fun getTimeOfTrackInPlaylist(playlistId: Int): List<String>
}
