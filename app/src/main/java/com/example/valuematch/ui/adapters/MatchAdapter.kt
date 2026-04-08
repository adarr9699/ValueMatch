package com.example.valuematch.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.valuematch.data.models.MatchItem
import com.example.valuematch.databinding.ItemMatchRowBinding

class MatchAdapter(
    private val onClick: (MatchItem) -> Unit
) : RecyclerView.Adapter<MatchAdapter.VH>() {

    private val items = mutableListOf<MatchItem>()

    fun submit(list: List<MatchItem>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val b = ItemMatchRowBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return VH(b, onClick)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(items[position])
    }

    class VH(
        private val b: ItemMatchRowBinding,
        private val onClick: (MatchItem) -> Unit
    ) : RecyclerView.ViewHolder(b.root) {

        fun bind(item: MatchItem) {
            b.matchName.text = item.otherName

            if (item.otherPhotoUrl.isNotBlank()) {
                b.matchImage.load(item.otherPhotoUrl)
            } else {
                b.matchImage.setImageResource(
                    com.example.valuematch.R.drawable.ic_profile
                )
            }

            b.root.setOnClickListener { onClick(item) }
        }
    }
}