package com.example.playlistmaker.utils

import android.content.Context
import android.util.TypedValue
import com.example.playlistmaker.db.data.PlaylistEntity
import com.example.playlistmaker.db.data.TrackEntity
import com.example.playlistmaker.media.domain.model.Playlist
import com.example.playlistmaker.search.data.dto.TrackDto
import com.example.playlistmaker.search.domain.model.Track
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Locale

object Utils {
    fun dpToPx(
        dp: Float,
        context: Context,
    ): Int =
        TypedValue
            .applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                context.resources.displayMetrics,
            ).toInt()

    fun timeConverter(time: Long?): String = SimpleDateFormat("mm:ss", Locale.getDefault()).format(time)

    fun map(trackDto: TrackDto): Track {
        return Track(
            trackDto.trackName,
            trackDto.artistName,
            timeConverter(trackDto.trackTimeMillis),
            trackDto.artworkUrl100,
            trackDto.collectionName,
            trackDto.releaseDate,
            trackDto.primaryGenreName,
            trackDto.country,
            trackDto.previewUrl,
        )
    }

    fun map(trackEntity: TrackEntity): Track {
        return Track(
            trackEntity.trackName,
            trackEntity.artistName,
            trackEntity.trackTime,
            trackEntity.artworkUrl100,
            trackEntity.collectionName,
            trackEntity.releaseDate,
            trackEntity.primaryGenreName,
            trackEntity.country,
            trackEntity.previewUrl,
        )
    }

    fun map(track: Track): TrackEntity {
        return TrackEntity(
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
    }

    fun map(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(
            playlist.name,
            playlist.desc,
            playlist.imageUri.toString(),
            Gson().toJson(playlist.tracks),
            playlist.tracksAmount
        )
    }
}
