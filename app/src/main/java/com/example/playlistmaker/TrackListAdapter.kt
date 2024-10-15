package com.example.playlistmaker

import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.api.Track
import com.example.playlistmaker.audioplayer.AudioPlayerActivity
import com.example.playlistmaker.utils.SharedPreferences
import java.text.SimpleDateFormat
import java.util.Locale

class TrackListAdapter(
    private val trackList: ArrayList<Track>,
) : RecyclerView.Adapter<TrackListAdapter.TrackListViewHolder>() {
    override fun onBindViewHolder(
        holder: TrackListViewHolder,
        position: Int,
    ) {
        holder.bind(trackList[position])
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): TrackListViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.track_item_list, parent, false)
        return TrackListViewHolder(view)
    }

    override fun getItemCount(): Int = trackList.size

    class TrackListViewHolder(
        itemView: View,
    ) : RecyclerView.ViewHolder(itemView) {
        private val trackItem = itemView.findViewById<View>(R.id.track_item)
        private val poster = itemView.findViewById<ImageView>(R.id.poster)
        private val trackName = itemView.findViewById<TextView>(R.id.track_name)
        private val trackTime = itemView.findViewById<TextView>(R.id.track_time)
        private val artists = itemView.findViewById<TextView>(R.id.artist)

        fun bind(track: Track) {
            trackName.text = track.trackName
            trackTime.text =
                SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)
            artists.text = track.artistName
            Glide
                .with(itemView)
                .load(track.artworkUrl100)
                .placeholder(R.drawable.placeholder)
                .fitCenter()
                .transform(RoundedCorners(dpToPx(2.0F, itemView.context)))
                .into(poster)
            trackItem.setOnClickListener {
                SharedPreferences.putTrackToHistory(itemView.context, track)
                AudioPlayerActivity.launch(itemView.context, track)
            }
        }

        private fun dpToPx(
            dp: Float,
            context: Context,
        ): Int =
            TypedValue
                .applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    dp,
                    context.resources.displayMetrics,
                ).toInt()
    }
}
