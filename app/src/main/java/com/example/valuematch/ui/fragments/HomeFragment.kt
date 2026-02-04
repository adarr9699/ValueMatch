package com.example.valuematch.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.valuematch.R
import com.example.valuematch.databinding.FragmentHomeBinding
import com.example.valuematch.repository.AuthRepository

class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val authRepo = AuthRepository()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)

        binding.goToSwipe.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_swipeFragment)
        }

        binding.goToMatches.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_matchesFragment)
        }

        binding.goToProfile.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_profileFragment)
        }

        binding.goToValues.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_valuesFragment)
        }

        binding.goToMessagesButton.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_matchesFragment)
        }

        binding.goToSettingsButton.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_settingsFragment)
        }

        binding.logoutButton.setOnClickListener {
            authRepo.logout()
            findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
