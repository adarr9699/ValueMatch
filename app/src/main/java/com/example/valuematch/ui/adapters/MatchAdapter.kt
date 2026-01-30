package com.example.valuematch.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.valuematch.data.models.MatchResult
import com.example.valuematch.databinding.ItemMatchBinding

class MatchAdapter(
    private val onClick: (MatchResult) -> Unit
) : RecyclerView.Adapter<MatchAdapter.VH>() {

    private val items = mutableListOf<MatchResult>()

    fun submit(list: List<MatchResult>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemMatchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(items[position])

    override fun getItemCount(): Int = items.size

    inner class VH(private val binding: ItemMatchBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MatchResult) {
            binding.matchName.text = item.otherName.ifBlank { item.otherUserId }
            binding.matchSharedValues.text = "Shared: ${item.sharedValues.joinToString(", ")}"
            binding.root.setOnClickListener { onClick(item) }
        }
    }
}
