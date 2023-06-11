package me.meenagopal24.wallpapers.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import me.meenagopal24.wallpapers.R
import me.meenagopal24.wallpapers.interfaces.ChangeInterface
import me.meenagopal24.wallpapers.models.wallpapers

class CategoryAdapter(private val list: MutableList<wallpapers.item>, val change: ChangeInterface) :
    RecyclerView.Adapter<CategoryAdapter.Holder>() {
    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView = itemView.findViewById(R.id.cat_title)
        val bg: ImageView = itemView.findViewById(R.id.cat_bg)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.category_single, parent, false)
        return Holder(view)

    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        Glide.with(holder.itemView.context).load(list[position].url).into(holder.bg)
        holder.title.text = list[position].name
        holder.bg.setOnClickListener {
            change.changeFragment(list[position].name, list[position].name)
        }
    }
}