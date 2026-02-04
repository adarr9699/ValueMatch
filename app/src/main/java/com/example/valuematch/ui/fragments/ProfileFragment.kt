package com.example.valuematch.ui.fragments

import android.content.Context
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

    private val prefsName = "valuematch_prefs"


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentProfileBinding.bind(view)

        val uid = FirebaseUtils.uidOrThrow()
        val prefs = requireContext().getSharedPreferences(prefsName, Context.MODE_PRIVATE)


        userRepo.getUser(uid).addOnSuccessListener { snap ->
            val u = snap.toObject(User::class.java)

            binding.nameInput.setText(u?.name ?: "")
            binding.bioInput.setText(u?.bio ?: "")
            binding.locationInput.setText(u?.location ?: "")

            when ((u?.gender ?: "").lowercase()) {
                "male" -> binding.genderMale.isChecked = true
                "female" -> binding.genderFemale.isChecked = true
            }

            binding.valuesLabel.text =
                "Values: ${(u?.values ?: emptyList()).joinToString(", ")}"
        }

        binding.editValuesButton.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_valuesFragment)
        }

        binding.saveProfileButton.setOnClickListener {
            val name = binding.nameInput.text.toString().trim()
            val bio = binding.bioInput.text.toString().trim()
            val location = binding.locationInput.text.toString().trim()

            val gender = when (binding.genderGroup.checkedRadioButtonId) {
                R.id.genderMale -> "male"
                R.id.genderFemale -> "female"
                else -> ""
            }

            if (name.isBlank()) {
                Toast.makeText(requireContext(), "Name required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (gender.isBlank()) {
                Toast.makeText(requireContext(), "Select Male or Female", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (location.isBlank()) {
                Toast.makeText(requireContext(), "Location required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val updatedUser = User(
                userId = uid,
                name = name,
                bio = bio,
                gender = gender,
                location = location
            )

            userRepo.upsertUser(updatedUser)
                .addOnSuccessListener {
                    Toast.makeText(requireContext(), "Profile saved ✅", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_profileFragment_to_homeFragment)
                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "Save failed", Toast.LENGTH_SHORT).show()
                }
        }

        binding.logoutButton.setOnClickListener {
            authRepo.logout()
            findNavController().navigate(R.id.action_profileFragment_to_loginFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
