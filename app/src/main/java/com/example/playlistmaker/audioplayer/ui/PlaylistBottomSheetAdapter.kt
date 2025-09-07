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

class PlaylistBottomSheetAdapter(
    private val playlists: List<Playlist>,
    private val clickListener: PlaylistClickListener
) :
    RecyclerView.Adapter<PlaylistBottomSheetAdapter.PlaylistBottomSheetViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlaylistBottomSheetViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.playlist_bottom_sheet_item, parent, false)
        return PlaylistBottomSheetViewHolder(view)
    }

    override fun getItemCount() = playlists.size

    override fun onBindViewHolder(holder: PlaylistBottomSheetViewHolder, position: Int) {
        holder.bind(playlists[position])
        holder.itemView.setOnClickListener { clickListener.onPlaylistClick(playlists[position]) }
    }

    class PlaylistBottomSheetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val image = itemView.findViewById<ImageView>(R.id.playlist_image)
        private val name = itemView.findViewById<TextView>(R.id.playlist_name)
        private val amount = itemView.findViewById<TextView>(R.id.playlist_amount)
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
                .transform(RoundedCorners(Utils.dpToPx(2.0F, itemView.context)))
                .into(image)
        }
    }

    fun interface PlaylistClickListener {
        fun onPlaylistClick(playlist: Playlist)
    }
}