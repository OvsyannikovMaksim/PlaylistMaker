package com.example.playlistmaker.media.domain

import com.example.playlistmaker.media.domain.model.Playlist
import com.example.playlistmaker.media.domain.repository.PlaylistRepository
import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

class PlaylistInteractorImpl(private val playlistRepository: PlaylistRepository) :
    PlaylistInteractor {

    override suspend fun addTrackToPlaylist(playlist: Playlist, track: Track): Boolean {
        return withContext(Dispatchers.IO) {
            var trackId = playlistRepository.getTrackId(track) ?: 0
            val res = playlistRepository.isTrackInPlaylist(playlist.id, trackId)
            if (!res) {
                playlistRepository.addPlayList(playlist)
                if (trackId == 0) {
                    playlistRepository.addTrack(track)
                    trackId = playlistRepository.getTrackId(track) ?: 0
                }
                playlistRepository.addTrackToPlaylist(playlist.id, trackId)
            }
            !res
        }
    }

    override suspend fun addPlayList(playlist: Playlist) {
        withContext(Dispatchers.IO) {
            playlistRepository.addPlayList(playlist)
        }
    }

    override fun getPlaylists(): Flow<List<Playlist>> {
        return playlistRepository.getPlaylists()
    }

    override fun getTracksInPlaylist(playlistId: Int): Flow<List<Track>> {
        return playlistRepository.getTracksForPlaylist(playlistId)
    }

    override suspend fun deleteTrack(playlistId: Int, track: Track) {
        playlistRepository.deleteTrackFromPlaylistToTrack(playlistId, track)
        if (playlistRepository.isAnyPlaylistContainsTrack(track)) {
            playlistRepository.deleteTrackFromTracks(track)
        }
    }

    override fun getPlaylistInfo(playlistId: Int): Flow<Playlist> {
        return playlistRepository.getPlaylistInfo(playlistId)
    }

    override suspend fun deletePlaylist(playlistId: Int) {
        playlistRepository.deletePlaylist(playlistId)
        playlistRepository.deletePlaylistFromPlaylistToTrack(playlistId)
    }

    override suspend fun getTimeOfTrackInPlaylist(playlistId: Int): Int {
        val tracksTime = playlistRepository.getTimeOfTrackInPlaylist(playlistId)
        var sumTimeInMills = 0L
        tracksTime.forEach {
            val time = it.split(':')
            val minToMills = TimeUnit.MINUTES.toMillis(time[0].toLong())
            val secToMills = TimeUnit.SECONDS.toMillis(time[1].toLong())
            sumTimeInMills+=(minToMills+secToMills)
        }
        return TimeUnit.MILLISECONDS.toMinutes(sumTimeInMills).toInt()
    }
}