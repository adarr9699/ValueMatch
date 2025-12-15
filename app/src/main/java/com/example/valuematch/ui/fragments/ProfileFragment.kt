package com.example.valuematch.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.valuematch.databinding.FragmentProfileSetupBinding
import com.example.valuematch.viewModel.ProfileViewModel

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileSetupBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileSetupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.userProfile.observe(viewLifecycleOwner) { profile ->
            binding.nameText.text = profile.name
            binding.ageText.text = "Age: ${profile.age}"
            binding.aboutText.text = profile.about
        }

        viewModel.loadProfile()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}