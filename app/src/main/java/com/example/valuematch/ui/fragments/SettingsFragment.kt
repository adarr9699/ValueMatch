package com.example.valuematch.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.valuematch.R
import com.example.valuematch.databinding.FragmentSettingsBinding
import com.example.valuematch.repository.AuthRepository

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private val authRepo = AuthRepository()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSettingsBinding.bind(view)

        val prefs = requireContext()
            .getSharedPreferences("valuematch_prefs", Context.MODE_PRIVATE)

        binding.darkModeSwitch.isChecked =
            prefs.getBoolean("dark_mode", false)

        binding.darkModeSwitch.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("dark_mode", isChecked).apply()

            AppCompatDelegate.setDefaultNightMode(
                if (isChecked)
                    AppCompatDelegate.MODE_NIGHT_YES
                else
                    AppCompatDelegate.MODE_NIGHT_NO
            )
        }

        binding.editProfileBtn.setOnClickListener {
            findNavController()
                .navigate(R.id.action_settingsFragment_to_profileFragment)
        }

        binding.editValuesBtn.setOnClickListener {
            findNavController()
                .navigate(R.id.action_settingsFragment_to_valuesFragment)
        }

        binding.logoutBtn.setOnClickListener {
            authRepo.logout()
            findNavController()
                .navigate(R.id.action_settingsFragment_to_loginFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
