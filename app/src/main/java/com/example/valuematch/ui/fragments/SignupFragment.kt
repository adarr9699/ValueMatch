package com.example.valuematch.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.valuematch.R
import com.example.valuematch.data.models.User
import com.example.valuematch.databinding.FragmentSignupBinding
import com.example.valuematch.repository.AuthRepository
import com.example.valuematch.repository.UserRepository
import com.example.valuematch.utils.FirebaseUtils

class SignupFragment : Fragment(R.layout.fragment_signup) {

    private var _binding: FragmentSignupBinding? = null
    private val binding get() = _binding!!

    private val authRepo = AuthRepository()
    private val userRepo = UserRepository()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSignupBinding.bind(view)

        binding.signupButton.setOnClickListener {
            val email = binding.emailInput.text.toString().trim()
            val pass = binding.passwordInput.text.toString().trim()
            val name = binding.nameInput.text.toString().trim()

            if (email.isBlank() || pass.isBlank() || name.isBlank()) {
                Toast.makeText(requireContext(), "Fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            authRepo.signup(email, pass)
                .addOnSuccessListener {
                    val uid = FirebaseUtils.uidOrThrow()

                    val user = User(
                        userId = uid,
                        name = name,
                        bio = "",
                        values = emptyList()
                    )

                    userRepo.upsertUser(user)
                        .addOnSuccessListener {
                            findNavController()
                                .navigate(R.id.action_signupFragment_to_valuesFragment)
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(
                                requireContext(),
                                e.message ?: "Failed to save profile",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(
                        requireContext(),
                        e.message ?: "Signup failed",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }

        binding.goToLogin.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

