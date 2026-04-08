package com.example.valuematch.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.valuematch.R
import com.example.valuematch.databinding.FragmentSettingsBinding
import com.google.firebase.auth.FirebaseAuth

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSettingsBinding.bind(view)

        binding.btnLogout.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Log out?")
                .setMessage("Are you sure you want to log out?")
                .setPositiveButton("Log out") { _, _ ->
                    FirebaseAuth.getInstance().signOut()
                    findNavController().navigate(R.id.action_settingsFragment_to_landingFragment)
                }
                .setNegativeButton("Cancel", null)
                .show()
        }

        binding.btnDeleteAccount.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Delete account?")
                .setMessage("This cannot be undone. Are you sure?")
                .setPositiveButton("Delete") { _, _ ->
                    val user = FirebaseAuth.getInstance().currentUser
                    if (user == null) {
                        findNavController().navigate(R.id.action_settingsFragment_to_landingFragment)
                        return@setPositiveButton
                    }

                    user.delete()
                        .addOnSuccessListener {
                            findNavController().navigate(R.id.action_settingsFragment_to_landingFragment)
                        }
                        .addOnFailureListener {
                            AlertDialog.Builder(requireContext())
                                .setTitle("Couldn’t delete")
                                .setMessage("Firebase may require you to log in again before deleting. Log out and log back in, then try again.")
                                .setPositiveButton("OK", null)
                                .show()
                        }
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}