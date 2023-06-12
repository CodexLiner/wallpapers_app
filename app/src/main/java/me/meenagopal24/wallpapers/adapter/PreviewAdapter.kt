package me.meenagopal24.wallpapers.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jsibbold.zoomage.ZoomageView
import me.meenagopal24.wallpapers.R
import me.meenagopal24.wallpapers.models.wallpapers

class PreviewAdapter(private val list: MutableList<wallpapers.item>?)  : RecyclerView.Adapter<PreviewAdapter.holder>() {
    class holder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val wallpaper : ZoomageView  = itemView.findViewById(R.id.wallpaper_container)
        val details : TextView = itemView.findViewById(R.id.wallpaper_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): holder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.wallpaper_single, parent, false)
        return holder(view)
    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    override fun onBindViewHolder(holder: holder, position: Int) {
        holder.details.text = list?.get(position)?.name ?: ""
        Glide.with(holder.wallpaper).load(list?.get(position)?.url).placeholder(R.drawable.grey).into(holder.wallpaper)
    }
}