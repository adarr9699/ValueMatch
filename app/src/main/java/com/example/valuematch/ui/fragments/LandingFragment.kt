package com.example.valuematch.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.valuematch.R
import com.example.valuematch.databinding.FragmentLandingBinding
import com.google.firebase.auth.FirebaseAuth

class LandingFragment : Fragment(R.layout.fragment_landing) {

    private var _binding: FragmentLandingBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentLandingBinding.bind(view)

        if (FirebaseAuth.getInstance().currentUser != null) {
            findNavController().navigate(R.id.action_landingFragment_to_homeFragment)
            return
        }

        binding.loginBtn.setOnClickListener {
            findNavController().navigate(R.id.action_landingFragment_to_loginFragment)
        }

        binding.signupBtn.setOnClickListener {
            findNavController().navigate(R.id.action_landingFragment_to_signupFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}