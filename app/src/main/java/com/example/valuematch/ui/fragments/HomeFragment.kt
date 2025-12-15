package com.example.valuematch.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.valuematch.R
import androidx.fragment.app.Fragment
import com.example.valuematch.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Button: Go to Match screen
        binding.goToMatchesBtn.setOnClickListener {
            findNavController().navigate(
                R.id.action_homeFragment_to_matchFragment
            )
        }

        // Button: Go to Profile screen
        binding.goToProfileBtn.setOnClickListener {
            findNavController().navigate(
                R.id.action_homeFragment_to_profileFragment
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
