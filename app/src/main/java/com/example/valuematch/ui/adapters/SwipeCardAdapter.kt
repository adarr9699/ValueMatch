package com.example.valuematch.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.valuematch.R
import com.example.valuematch.data.models.SwipeCard
import com.example.valuematch.databinding.ItemSwipeCardBinding

class SwipeCardAdapter(
    private val items: MutableList<SwipeCard>
) : RecyclerView.Adapter<SwipeCardAdapter.VH>() {

    private var swipeProgress: Float = 0f
    var lastRemoved: SwipeCard? = null
        private set

    fun submit(list: List<SwipeCard>) {
        items.clear()
        items.addAll(list)
        swipeProgress = 0f
        notifyDataSetChanged()
    }

    fun peekTop(): SwipeCard? = items.firstOrNull()

    fun removeTop(): SwipeCard? {
        if (items.isEmpty()) return null
        lastRemoved = items.removeAt(0)
        notifyItemRemoved(0)
        return lastRemoved
    }

    fun setSwipeProgress(progress: Float) {
        swipeProgress = progress
        if (items.isNotEmpty()) notifyItemChanged(0)
    }

    fun clearStamps() {
        swipeProgress = 0f
        if (items.isNotEmpty()) notifyItemChanged(0)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val b = ItemSwipeCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return VH(b)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(items[position], swipeProgress)
    }

    class VH(private val b: ItemSwipeCardBinding) : RecyclerView.ViewHolder(b.root) {

        fun bind(card: SwipeCard, progress: Float) {
            b.cardNameAge.text = "${card.name}, ${card.age}"

            if (card.photoUrl.isNotBlank()) {
                b.cardPhoto.load(card.photoUrl)
            } else {
                b.cardPhoto.setImageResource(R.drawable.ic_profile)
            }

            val values = card.bioLine.split("•").map { it.trim() }.filter { it.isNotBlank() }

            bindChip(b.chip1, values.getOrNull(0))
            bindChip(b.chip2, values.getOrNull(1))
            bindChip(b.chip3, values.getOrNull(2))

            if (progress > 0.1f) {
                b.stampLike.visibility = View.VISIBLE
                b.stampNope.visibility = View.GONE
                b.stampLike.alpha = progress.coerceIn(0f, 1f)
            } else if (progress < -0.1f) {
                b.stampLike.visibility = View.GONE
                b.stampNope.visibility = View.VISIBLE
                b.stampNope.alpha = (-progress).coerceIn(0f, 1f)
            } else {
                b.stampLike.visibility = View.GONE
                b.stampNope.visibility = View.GONE
            }
        }

        private fun bindChip(chip: com.google.android.material.chip.Chip, text: String?) {
            if (text.isNullOrBlank()) {
                chip.visibility = View.GONE
            } else {
                chip.visibility = View.VISIBLE
                chip.text = text
            }
        }
    }
}