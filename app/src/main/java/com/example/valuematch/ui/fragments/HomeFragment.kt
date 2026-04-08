package com.example.valuematch.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.valuematch.R
import com.example.valuematch.databinding.FragmentHomeBinding

class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)


        val childNavHost =
            childFragmentManager.findFragmentById(R.id.homeContainer) as NavHostFragment
        val childNavController = childNavHost.navController
        binding.bottomNav.setupWithNavController(childNavController)


        binding.btnSettings.setOnClickListener {
            findNavController().navigate(R.id.settingsFragment)
        }

//        binding.btnMessages.setOnClickListener {
//            findNavController().navigate(R.id.myMatchesFragment)
//        }

        binding.appLogo.setOnClickListener {
            childNavController.navigate(R.id.swipeFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}