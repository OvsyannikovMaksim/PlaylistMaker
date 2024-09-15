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

class TrackListAdapter(private val trackList: ArrayList<Track>) :
    RecyclerView.Adapter<TrackListAdapter.TrackListViewHolder>() {

    override fun onBindViewHolder(holder: TrackListViewHolder, position: Int) {
        holder.bind(trackList[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackListViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.track_item_list, parent, false)
        return TrackListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return trackList.size
    }

    class TrackListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
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

        private fun dpToPx(dp: Float, context: Context): Int {
            return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                context.resources.displayMetrics
            ).toInt()
        }
    }
}

