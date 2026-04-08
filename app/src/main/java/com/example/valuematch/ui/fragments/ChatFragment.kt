package com.example.valuematch.ui.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.valuematch.R
import com.example.valuematch.databinding.FragmentChatBinding
import com.example.valuematch.repository.MessageRepository
import com.example.valuematch.ui.adapters.ChatAdapter
import com.example.valuematch.utils.uidOrThrow
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class ChatFragment : Fragment(R.layout.fragment_chat) {

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!

    private val repo = MessageRepository()
    private val db = FirebaseFirestore.getInstance()
    private var listener: ListenerRegistration? = null

    private lateinit var adapter: ChatAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentChatBinding.bind(view)

        val myUid = uidOrThrow()

        val otherUid = arguments?.getString("otherUserId").orEmpty()
        if (otherUid.isBlank()) {
            Toast.makeText(requireContext(), "Missing other user", Toast.LENGTH_SHORT).show()
            return
        }

        adapter = ChatAdapter(myUid)
        binding.messagesRecycler.layoutManager = LinearLayoutManager(requireContext()).apply {
            stackFromEnd = true
        }
        binding.messagesRecycler.adapter = adapter

        listener = repo.listenMessages(otherUid) { list ->
            adapter.submit(list)
            if (list.isNotEmpty()) binding.messagesRecycler.scrollToPosition(list.size - 1)
        }

        binding.sendButton.setOnClickListener {
            val text = binding.messageInput.text.toString().trim()
            if (text.isBlank()) return@setOnClickListener

            repo.sendMessage(otherUid, text)
            binding.messageInput.setText("")
        }

        binding.btnMore.setOnClickListener { anchor ->
            val menu = PopupMenu(requireContext(), anchor)
            menu.menuInflater.inflate(R.menu.menu_chat_more, menu.menu)

            menu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.action_block -> {
                        confirm(
                            title = "Block this user?",
                            message = "They won’t be able to message you."
                        ) { blockUser(myUid, otherUid) }
                        true
                    }
                    R.id.action_report -> {
                        confirm(
                            title = "Report this user?",
                            message = "We’ll record this report."
                        ) { reportUser(myUid, otherUid) }
                        true
                    }
                    else -> false
                }
            }

            menu.show()
        }
    }

    private fun confirm(title: String, message: String, onYes: () -> Unit) {
        AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setNegativeButton("Cancel", null)
            .setPositiveButton("Yes") { _, _ -> onYes() }
            .show()
    }

    private fun blockUser(myUid: String, otherUid: String) {
        val docId = "${myUid}_$otherUid"
        db.collection("blocks")
            .document(docId)
            .set(
                mapOf(
                    "from" to myUid,
                    "to" to otherUid,
                    "createdAt" to FieldValue.serverTimestamp()
                )
            )
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "User blocked", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), e.message ?: "Failed to block", Toast.LENGTH_SHORT).show()
            }
    }

    private fun reportUser(myUid: String, otherUid: String) {
        db.collection("reports")
            .add(
                mapOf(
                    "from" to myUid,
                    "to" to otherUid,
                    "createdAt" to FieldValue.serverTimestamp()
                )
            )
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Report sent", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), e.message ?: "Failed to report", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        listener?.remove()
        listener = null
        _binding = null
    }
}