package com.example.valuematch.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.example.valuematch.R
import com.example.valuematch.databinding.FragmentHomeBinding
import com.example.valuematch.utils.FirebaseUtils.auth
import com.google.firebase.auth.FirebaseAuth



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
        binding.matchesBtn.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_matchFragment)
        }

        binding.profileBtn.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_profileFragment)
        }

        binding.logoutBtn.setOnClickListener {
           findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
        }

        fun logoutUser() {

            auth.signOut()


            findNavController().navigate(
                R.id.loginFragment,
                null,
                navOptions {
                    popUpTo(R.id.homeFragment) {
                        inclusive = true
                    }
                }
            )
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}