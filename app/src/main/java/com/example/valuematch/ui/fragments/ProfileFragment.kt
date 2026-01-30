package com.example.valuematch.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.valuematch.R
import com.example.valuematch.data.models.User
import com.example.valuematch.databinding.FragmentProfileBinding
import com.example.valuematch.repository.AuthRepository
import com.example.valuematch.repository.UserRepository
import com.example.valuematch.utils.FirebaseUtils

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val userRepo = UserRepository()
    private val authRepo = AuthRepository()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentProfileBinding.bind(view)

        val uid = FirebaseUtils.uidOrThrow()

        userRepo.getUser(uid).addOnSuccessListener { snap ->
            val u = snap.toObject(User::class.java)

            binding.nameInput.setText(u?.name ?: "")
            binding.bioInput.setText(u?.bio ?: "")
            binding.valuesLabel.text =
                "Values: ${(u?.values ?: emptyList()).joinToString(", ")}"

            setSpinner(binding.genderSpinner, u?.gender)
            setSpinner(binding.interestedInSpinner, u?.interestedIn)
        }

        binding.editValuesButton.setOnClickListener {
            findNavController()
                .navigate(R.id.action_profileFragment_to_valuesFragment)
        }

        binding.saveProfileButton.setOnClickListener {
            val name = binding.nameInput.text.toString().trim()
            val bio = binding.bioInput.text.toString().trim()
            val gender = binding.genderSpinner.selectedItem.toString()
            val interestedIn = binding.interestedInSpinner.selectedItem.toString()

            if (name.isBlank()) {
                Toast.makeText(requireContext(), "Name required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            userRepo.getUser(uid).addOnSuccessListener { snap ->
                val old = snap.toObject(User::class.java) ?: User(userId = uid)

                val updated = old.copy(
                    name = name,
                    bio = bio,
                    gender = gender,
                    interestedIn = interestedIn
                )

                userRepo.upsertUser(updated)
                    .addOnSuccessListener {
                        Toast.makeText(
                            requireContext(),
                            "Profile saved successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }
        }

        binding.logoutButton.setOnClickListener {
            authRepo.logout()
            findNavController()
                .navigate(R.id.action_profileFragment_to_loginFragment)
        }
    }

    private fun setSpinner(spinner: android.widget.Spinner, value: String?) {
        if (value.isNullOrBlank()) return
        val index = (0 until spinner.count)
            .firstOrNull { spinner.getItemAtPosition(it) == value }
            ?: return
        spinner.setSelection(index)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
