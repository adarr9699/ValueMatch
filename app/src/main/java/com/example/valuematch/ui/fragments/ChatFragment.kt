package com.example.valuematch.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.valuematch.R
import com.example.valuematch.databinding.FragmentChatBinding
import com.example.valuematch.repository.MessageRepository
import com.example.valuematch.ui.adapters.MessageAdapter
import com.example.valuematch.utils.FirebaseUtils

class ChatFragment : Fragment(R.layout.fragment_chat) {

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!

    private val msgRepo = MessageRepository()

    private lateinit var adapter: MessageAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentChatBinding.bind(view)

        val otherUid = ChatFragmentArgs.fromBundle(requireArguments()).otherUserId
        val myUid = FirebaseUtils.uidOrThrow()

        adapter = MessageAdapter(myUid)

        binding.messagesRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.messagesRecycler.adapter = adapter

        msgRepo.listenMessages(otherUid) { list ->
            adapter.submit(list)
            if (list.isNotEmpty()) binding.messagesRecycler.scrollToPosition(list.size - 1)
        }

        binding.sendButton.setOnClickListener {
            val text = binding.messageInput.text.toString().trim()
            if (text.isBlank()) return@setOnClickListener
            binding.messageInput.setText("")
            msgRepo.sendMessage(otherUid, text)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
