package com.example.playlistmaker.search.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.utils.Utils.dpToPx

class TrackListAdapter(
    private val trackList: List<Track>,
    private val clickListener: TrackClickListener,
) : RecyclerView.Adapter<TrackListAdapter.TrackListViewHolder>() {
    override fun onBindViewHolder(
        holder: TrackListViewHolder,
        position: Int,
    ) {
        holder.bind(trackList[position])
        holder.itemView.setOnClickListener { clickListener.onTrackClick(trackList[position]) }
        holder.itemView.setOnLongClickListener { clickListener.onLongTrackClick(trackList[position]) }
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
        private val poster = itemView.findViewById<ImageView>(R.id.poster)
        private val trackName = itemView.findViewById<TextView>(R.id.track_name)
        private val trackTime = itemView.findViewById<TextView>(R.id.track_time)
        private val artists = itemView.findViewById<TextView>(R.id.artist)

        fun bind(track: Track) {
            trackName.text = track.trackName
            trackTime.text = track.trackTime
            artists.text = track.artistName
            Glide.with(itemView)
                .load(track.artworkUrl100)
                .placeholder(R.drawable.placeholder)
                .fitCenter()
                .transform(RoundedCorners(dpToPx(2.0F, itemView.context)))
                .into(poster)
        }
    }

    interface TrackClickListener {
        fun onTrackClick(track: Track)
        fun onLongTrackClick(track: Track) = false
    }
}
