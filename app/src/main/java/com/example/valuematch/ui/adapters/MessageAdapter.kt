package com.example.valuematch.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.valuematch.data.models.Message
import com.example.valuematch.databinding.ItemMessageReceivedBinding
import com.example.valuematch.databinding.ItemMessageSentBinding

class MessageAdapter(
    private val myUid: String
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items = mutableListOf<Message>()

    fun submit(list: List<Message>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = items.size

    override fun getItemViewType(position: Int): Int {
        return if (items[position].fromUserId == myUid) VIEW_SENT else VIEW_RECEIVED
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == VIEW_SENT) {
            val binding = ItemMessageSentBinding.inflate(inflater, parent, false)
            SentVH(binding)
        } else {
            val binding = ItemMessageReceivedBinding.inflate(inflater, parent, false)
            ReceivedVH(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val msg = items[position]
        when (holder) {
            is SentVH -> holder.bind(msg)
            is ReceivedVH -> holder.bind(msg)
        }
    }

    class SentVH(private val binding: ItemMessageSentBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(m: Message) {
            binding.messageText.text = m.text
        }
    }

    class ReceivedVH(private val binding: ItemMessageReceivedBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(m: Message) {
            binding.messageText.text = m.text
        }
    }

    companion object {
        private const val VIEW_SENT = 1
        private const val VIEW_RECEIVED = 2
    }
}
