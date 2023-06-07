package me.meenagopal24.wallpapers;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

class ScalingItemDecoration(private val scaleValue: Float) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)
        val itemCount = state.itemCount

        if (position != RecyclerView.NO_POSITION && itemCount > 0) {
            val maxOffset = (scaleValue - 1) * view.height / 2
            val offset = maxOffset * (position.toFloat() / itemCount.toFloat())
            outRect.set(0, offset.toInt(), 0, offset.toInt())
        } else {
            outRect.setEmpty()
        }
    }
}