package com.example.playlistmaker.media.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.media.domain.model.Playlist
import com.example.playlistmaker.utils.Utils
import java.io.File

class PlaylistAdapter(private val playlists: List<Playlist>,
    private val listener: PlayListListener) :
    RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.playlist_item, parent, false)
        return PlaylistViewHolder(view)
    }

    override fun getItemCount() = playlists.size

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.bind(playlists[position])
        holder.itemView.setOnClickListener { listener.onPlaylistClick(playlists[position]) }
    }

    class PlaylistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val image = itemView.findViewById<ImageView>(R.id.image)
        private val name = itemView.findViewById<TextView>(R.id.name)
        private val amount = itemView.findViewById<TextView>(R.id.amount)
        fun bind(playlist: Playlist) {
            name.text = playlist.name
            amount.text = itemView.context.resources.getQuantityString(
                R.plurals.tracks_count,
                playlist.tracksAmount,
                playlist.tracksAmount
            )
            val imageUri = if(playlist.imagePath==null) {
                null
            } else {
                File(playlist.imagePath).toUri()
            }
            Glide.with(itemView)
                .load(imageUri)
                .placeholder(R.drawable.placeholder)
                .centerCrop()
                .transform(RoundedCorners(Utils.dpToPx(8.0F, itemView.context)))
                .into(image)
        }
    }

    fun interface PlayListListener {
        fun onPlaylistClick(playlist: Playlist)
    }
}