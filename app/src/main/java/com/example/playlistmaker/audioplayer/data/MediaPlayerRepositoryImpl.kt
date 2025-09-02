package com.example.playlistmaker.audioplayer.data

import android.media.MediaPlayer
import com.example.playlistmaker.audioplayer.domain.repository.MediaPlayerRepository
import com.example.playlistmaker.db.data.TrackDatabase
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.utils.Utils
import com.example.playlistmaker.utils.Utils.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MediaPlayerRepositoryImpl(
    private val mediaPlayer: MediaPlayer,
    private val trackDatabase: TrackDatabase
) : MediaPlayerRepository {

    override fun preparePlayer(
        url: String?,
        onPreparedListener: () -> Unit,
        onCompletionListener: () -> Unit
    ) {
        mediaPlayer.apply {
            setDataSource(url)
            prepareAsync()
            setOnPreparedListener { onPreparedListener() }
            setOnCompletionListener { onCompletionListener() }
        }
    }

    override fun startPlayer() {
        mediaPlayer.start()
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
    }

    override fun releasePlayer() {
        mediaPlayer.release()
    }

    override fun getCurrentTime(): String {
        return Utils.timeConverter(mediaPlayer.currentPosition.toLong())
    }

    override suspend fun addToFavourite(track: Track) {
        trackDatabase.trackDao().insertTrack(map(track))
    }

    override suspend fun removeFavourite(track: Track) {
        trackDatabase.trackDao().deleteTrack(map(track))
    }

    override suspend fun isTrackInFavourite(track: Track): Int {
        val trackEntity = map(track)
        return trackDatabase.trackDao().isTrackInFavourite(trackEntity.trackName, trackEntity.artistName)
    }
}