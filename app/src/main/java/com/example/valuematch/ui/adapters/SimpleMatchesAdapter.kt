package com.example.valuematch.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.valuematch.databinding.ItemMatchRowBinding

data class SimpleMatchRow(
    val otherUid: String,
    val otherName: String
)

class SimpleMatchesAdapter(
    private val onClick: (String) -> Unit
) : RecyclerView.Adapter<SimpleMatchesAdapter.VH>() {

    private val items = mutableListOf<SimpleMatchRow>()

    fun submitList(newList: List<SimpleMatchRow>) {
        items.clear()
        items.addAll(newList)
        notifyDataSetChanged()
    }

    inner class VH(val binding: ItemMatchRowBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemMatchRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = items[position]
        holder.binding.matchName.text = item.otherName
        holder.binding.root.setOnClickListener { onClick(item.otherUid) }
    }

    override fun getItemCount(): Int = items.size
}
