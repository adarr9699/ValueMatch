package com.example.valuematch.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.valuematch.R
import com.example.valuematch.data.models.User
import com.example.valuematch.databinding.FragmentMatchesBinding
import com.example.valuematch.repository.MatchItem
import com.example.valuematch.repository.MatchRepository
import com.example.valuematch.ui.adapters.SimpleMatchRow
import com.example.valuematch.ui.adapters.SimpleMatchesAdapter
import com.example.valuematch.repository.UserRepository
import com.example.valuematch.utils.FirebaseUtils
import com.google.firebase.firestore.ListenerRegistration

class MatchesFragment : Fragment(R.layout.fragment_matches) {

    private var _binding: FragmentMatchesBinding? = null
    private val binding get() = _binding!!

    private val matchRepo = MatchRepository()
    private val userRepo = UserRepository()

    private var listener: ListenerRegistration? = null

    private lateinit var adapter: SimpleMatchesAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMatchesBinding.bind(view)

        val myUid = FirebaseUtils.uidOrThrow()

        adapter = SimpleMatchesAdapter { otherUid ->
            val bundle = Bundle().apply { putString("otherUserId", otherUid) }
            findNavController().navigate(R.id.chatFragment, bundle)
        }

        binding.matchesRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.matchesRecycler.adapter = adapter

        listener = matchRepo.listenMatchesForCurrentUser(
            myUid = myUid,
            onUpdate = { matches: List<MatchItem> ->
                if (matches.isEmpty()) {
                    adapter.submitList(emptyList())
                    return@listenMatchesForCurrentUser
                }

                val rows = mutableListOf<SimpleMatchRow>()

                matches.forEach { m: MatchItem ->
                    val otherUid = m.otherUserId(myUid)

                    userRepo.getUser(otherUid).addOnSuccessListener { snap ->
                        val u = snap.toObject(User::class.java)
                        val name = u?.name?.ifBlank { otherUid } ?: otherUid

                        rows.add(SimpleMatchRow(otherUid = otherUid, otherName = name))

                        adapter.submitList(rows.sortedBy { it.otherName })
                    }
                }
            },
            onError = { e ->
                Toast.makeText(requireContext(), e.message ?: "Failed to load matches", Toast.LENGTH_SHORT).show()
            }
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        listener?.remove()
        listener = null
        _binding = null
    }
}



