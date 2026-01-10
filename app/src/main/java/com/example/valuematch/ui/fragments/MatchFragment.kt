package com.example.valuematch.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.valuematch.databinding.FragmentMatchBinding
import com.example.valuematch.viewmodel.MatchViewModel

class MatchFragment : Fragment() {

    private var _binding: FragmentMatchBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MatchViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMatchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel.bestMatches.observe(viewLifecycleOwner) { matches ->
            binding.resultsText.text = matches.joinToString("\n") {
                "${it.userId}: ${it.matchScore}%"
            }
        }


        viewModel.loadMatches()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}