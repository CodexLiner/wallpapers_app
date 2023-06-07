package me.meenagopal24.wallpapers.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import me.meenagopal24.wallpapers.R
import me.meenagopal24.wallpapers.interfaces.PreviewInterface

class StaggeredAdapter(private val list: List<String>, private val change : PreviewInterface) : RecyclerView.Adapter<StaggeredAdapter.Holder>() {
    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val sImage: ImageView = itemView.findViewById(R.id.staggered_image)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.staggered_single, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        Glide.with(holder.itemView.context).load(list[position]).placeholder(R.drawable.grey).into(holder.sImage)
        holder.sImage.setOnClickListener{
            change.changeFragment(position)
        }
    }
}