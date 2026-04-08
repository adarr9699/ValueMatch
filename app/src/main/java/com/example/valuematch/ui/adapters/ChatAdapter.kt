package com.example.valuematch.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.valuematch.data.models.ChatMessage
import com.example.valuematch.databinding.ItemChatMessageBinding

class ChatAdapter(
    private val myUid: String
) : RecyclerView.Adapter<ChatAdapter.VH>() {

    private val items = mutableListOf<ChatMessage>()

    fun submit(list: List<ChatMessage>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val b = ItemChatMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(b, myUid)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(items[position])
    }

    class VH(
        private val b: ItemChatMessageBinding,
        private val myUid: String
    ) : RecyclerView.ViewHolder(b.root) {

        fun bind(msg: ChatMessage) {
            b.msgText.text = msg.text
            // (Optional later: style based on msg.from == myUid)
        }
    }
}