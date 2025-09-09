package com.example.playlistmaker.media.data

import android.util.Log
import com.example.playlistmaker.db.data.AppDatabase
import com.example.playlistmaker.db.data.PlaylistToTrackEntity
import com.example.playlistmaker.db.data.TracksInPlaylistsEntity
import com.example.playlistmaker.media.domain.model.Playlist
import com.example.playlistmaker.media.domain.repository.PlaylistRepository
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.utils.Utils.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaylistRepositoryImpl(private val database: AppDatabase) : PlaylistRepository {
    override suspend fun addPlayList(playlist: Playlist) {
        database.playlistDao().insertPlaylist(map(playlist))
    }

    override suspend fun getPlaylists(): Flow<List<Playlist>> = flow {
        emit(database.playlistDao().getPlaylists().map { map(it) })
    }

    override suspend fun getTrackId(track: Track): Int? {
        return database.playlistDao().getTrackId(track.artistName, track.trackName)
    }

    override suspend fun getPlaylistId(playlist: Playlist): Int {
        return database.playlistDao().getPlaylistId(playlist.name)
    }

    override suspend fun isTrackInPlaylist(playlistId: Int, trackId: Int): Boolean {
        Log.d(
            "TEST",
            "isTrackInPlaylist: ${database.playlistDao().isTrackInPlaylist(playlistId, trackId)}"
        )
        return database.playlistDao().isTrackInPlaylist(playlistId, trackId) == 1
    }

    override suspend fun addTrack(track: Track) {
        database.playlistDao().addTrack(
            TracksInPlaylistsEntity(
                0,
                track.trackName,
                track.artistName,
                track.trackTime,
                track.artworkUrl100,
                track.collectionName,
                track.releaseDate,
                track.primaryGenreName,
                track.country,
                track.previewUrl,
            )
        )
    }

    override suspend fun addTrackToPlaylist(playlistId: Int, trackId: Int) {
        database.playlistDao().addTrackToPlaylist(PlaylistToTrackEntity(0, playlistId, trackId))
    }

    override suspend fun getTracksForPlaylist(playlistId: Int) = flow {
        emit(database.playlistDao().getTrackListForPlaylist(playlistId).map { map(it) })
    }

    override suspend fun deleteTrackFromPlaylistToTrack(playlistId: Int, track: Track) {
        database.playlistDao().deleteTrackFromPlaylistToTrack(playlistId, track.id)
    }

    override suspend fun deleteTrackFromTracks(track: Track) {
        database.playlistDao().deleteTrack(track.artistName, track.trackName)
    }

    override suspend fun isAnyPlaylistContainsTrack(track: Track): Boolean {
        return database.playlistDao().isAnyPlaylistContainsTrack(track.id) > 0
    }

    override suspend fun getPlaylistInfo(playlistId: Int) = flow {
        emit(map(database.playlistDao().getPlaylistInfo(playlistId)))
    }

    override suspend fun deletePlaylist(playlistId: Int) {
        database.playlistDao().deletePlaylist(playlistId)
    }

    override suspend fun deletePlaylistFromPlaylistToTrack(playlistId: Int) {
        database.playlistDao().deletePlaylistFromPlaylistToTrack(playlistId)
    }
}