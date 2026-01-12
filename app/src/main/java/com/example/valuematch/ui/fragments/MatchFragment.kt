package com.example.valuematch.ui.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.valuematch.databinding.FragmentMatchBinding
import com.example.valuematch.repository.AuthRepository
import com.example.valuematch.repository.LikeRepository
import com.example.valuematch.viewModel.MatchViewModel

class MatchFragment : Fragment() {

    private var _binding: FragmentMatchBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MatchViewModel by viewModels()
    private val authRepo = AuthRepository()
    private val likeRepo = LikeRepository()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMatchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel.matches.observe(viewLifecycleOwner) { matches ->
            val match = matches.firstOrNull() ?: return@observe
            binding.matchText.text = match.userId
        }

        binding.likeBtn.setOnClickListener {
            val uid = authRepo.currentUserId() ?: return@setOnClickListener
            val target = viewModel.currentMatchUserId ?: return@setOnClickListener
            likeRepo.like(uid, target)
        }

        binding.passBtn.setOnClickListener {
            viewModel.nextMatch()
        }

        viewModel.loadMatches()
    }
}
