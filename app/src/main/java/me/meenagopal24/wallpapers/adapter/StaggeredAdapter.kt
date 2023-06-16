package me.meenagopal24.wallpapers.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import me.meenagopal24.wallpapers.R
import me.meenagopal24.wallpapers.interfaces.ChangeInterface
import me.meenagopal24.wallpapers.models.wallpapers
import me.meenagopal24.wallpapers.utils.Constants.BASE_URL_IMAGE
import me.meenagopal24.wallpapers.utils.Functions

class StaggeredAdapter(
    private val list: MutableList<wallpapers.item>,
    private val change: ChangeInterface,
) :
    RecyclerView.Adapter<StaggeredAdapter.Holder>() {
    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val sImage: ImageView = itemView.findViewById(R.id.staggered_image)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.staggered_single, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return list.size ?: 0
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        Glide.with(holder.itemView.context)
            .load(BASE_URL_IMAGE + list[position].category.trim() + "/" + list[position].image.trim())
            .diskCacheStrategy(DiskCacheStrategy.DATA)
            .placeholder(R.drawable.grey).into(holder.sImage)
        holder.sImage.setOnClickListener {
            change.changeFragment(position)
        }
    }
}