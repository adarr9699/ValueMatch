package com.example.valuematch.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.valuematch.R
import com.example.valuematch.data.models.User
import com.example.valuematch.databinding.FragmentSwipeBinding
import com.example.valuematch.repository.LikeRepository
import com.example.valuematch.repository.MatchRepository
import com.example.valuematch.repository.UserRepository
import com.example.valuematch.utils.FirebaseUtils
import com.google.firebase.firestore.ListenerRegistration

class SwipeFragment : Fragment(R.layout.fragment_swipe) {

    private var _binding: FragmentSwipeBinding? = null
    private val binding get() = _binding!!

    private val userRepo = UserRepository()
    private val likeRepo = LikeRepository()
    private val matchRepo = MatchRepository()

    private var listener: ListenerRegistration? = null

    private val queue = mutableListOf<User>()
    private var currentUser: User? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSwipeBinding.bind(view)

        val myUid = FirebaseUtils.uidOrThrow()

        listener = userRepo.listenAllUsers(
            onUpdate = { users ->
                val others = users.filter { it.userId.isNotBlank() && it.userId != myUid }

                queue.clear()
                queue.addAll(others)

                if (queue.isNotEmpty()) {
                    showNext()
                } else {
                    showEmpty()
                }
            },
            onError = {
                Toast.makeText(requireContext(), "Failed to load users", Toast.LENGTH_SHORT).show()
            }
        )

        binding.likeButton.setOnClickListener {
            val me = FirebaseUtils.uidOrThrow()
            val other = currentUser?.userId.orEmpty()

            if (other.isBlank()) {
                Toast.makeText(requireContext(), "No user selected", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            likeRepo.saveLike(me, other)
                .addOnSuccessListener {
                    likeRepo.didLike(other, me)
                        .addOnSuccessListener { doc ->
                            if (doc.exists()) {
                                matchRepo.createMatch(me, other)
                                    .addOnSuccessListener {
                                        Toast.makeText(requireContext(), "It's a match! 🎉", Toast.LENGTH_SHORT).show()

                                        val args = Bundle().apply {
                                            putString("otherUserId", other)
                                        }
                                        findNavController().navigate(
                                            R.id.action_swipeFragment_to_chatFragment,
                                            args
                                        )
                                    }
                                    .addOnFailureListener { e ->
                                        Toast.makeText(
                                            requireContext(),
                                            e.message ?: "Match create failed",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                            } else {
                                Toast.makeText(
                                    requireContext(),
                                    "Liked! Waiting for them to like back.",
                                    Toast.LENGTH_SHORT
                                ).show()
                                showNext()
                            }
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(
                                requireContext(),
                                e.message ?: "Failed to check match",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(requireContext(), e.message ?: "Failed to like", Toast.LENGTH_SHORT).show()
                }
        }

        binding.passButton.setOnClickListener { showNext() }

        binding.goToMatches.setOnClickListener {
            findNavController().navigate(R.id.action_swipeFragment_to_matchesFragment)
        }
    }

    private fun showNext() {
        if (queue.isEmpty()) {
            showEmpty()
            return
        }

        val user = queue.removeAt(0)
        currentUser = user

        binding.cardName.text = user.name.ifBlank { "Unnamed user" }
        binding.cardBio.text = user.bio.ifBlank { "No bio provided" }
        binding.cardValues.text =
            if (user.values.isNotEmpty()) "Values: ${user.values.joinToString(", ")}"
            else "Values: none yet"
    }

    private fun showEmpty() {
        currentUser = null
        binding.cardName.text = "No users yet"
        binding.cardBio.text = "Create another account to test swiping"
        binding.cardValues.text = ""
    }

    override fun onDestroyView() {
        super.onDestroyView()
        listener?.remove()
        listener = null
        _binding = null
    }
}
