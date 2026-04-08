package com.example.valuematch.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.valuematch.R
import com.example.valuematch.data.models.MatchItem
import com.example.valuematch.data.models.User
import com.example.valuematch.databinding.FragmentMyMatchesBinding
import com.example.valuematch.repository.MatchRepository
import com.example.valuematch.ui.adapters.MatchAdapter
import com.example.valuematch.utils.FirebaseUtils
import com.example.valuematch.utils.uidOrThrow
import com.google.firebase.firestore.ktx.toObject

class MyMatchesFragment : Fragment(R.layout.fragment_my_matches) {

    private var _binding: FragmentMyMatchesBinding? = null
    private val binding get() = _binding!!

    private val matchRepo = MatchRepository()
    private lateinit var adapter: MatchAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMyMatchesBinding.bind(view)

        val myUid = uidOrThrow()

        adapter = MatchAdapter { item ->
            val b = Bundle().apply { putString("otherUserId", item.otherUserId) }
            findNavController().navigate(R.id.chatFragment, b)
        }

        binding.matchesRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.matchesRecycler.adapter = adapter

        matchRepo.listenOtherUserIdsForMatches(
            myUid = myUid,
            onUpdate = { otherUids ->
                if (otherUids.isEmpty()) {
                    adapter.submit(emptyList())
                    binding.emptyText.visibility = View.VISIBLE
                    return@listenOtherUserIdsForMatches
                }

                binding.emptyText.visibility = View.GONE

                val rows = mutableListOf<MatchItem>()
                var remaining = otherUids.size

                otherUids.forEach { otherUid ->
                    FirebaseUtils.getUser(otherUid)
                        .addOnSuccessListener { snap ->
                            val u = snap.toObject<User>()
                            val name = u?.name?.takeIf { it.isNotBlank() } ?: otherUid
                            val photoUrl = u?.photoUrl ?: ""
                            rows.add(
                                MatchItem(
                                    otherUserId = otherUid,
                                    otherName = name,
                                    otherPhotoUrl = photoUrl
                                )
                            )
                        }
                        .addOnFailureListener {
                            rows.add(MatchItem(otherUserId = otherUid, otherName = otherUid))
                        }
                        .addOnCompleteListener {
                            remaining -= 1
                            if (remaining == 0) {
                                adapter.submit(rows.sortedBy { it.otherName.lowercase() })
                            }
                        }
                }
            },
            onError = { e ->
                Toast.makeText(
                    requireContext(),
                    e.message ?: "Failed to load matches",
                    Toast.LENGTH_SHORT
                ).show()
            }
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}