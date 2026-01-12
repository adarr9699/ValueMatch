package com.example.valuematch.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.valuematch.databinding.FragmentSignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.example.valuematch.R

class SignupFragment : Fragment() {

    private var _binding: FragmentSignupBinding? = null
    private val binding get() = _binding!!

    private val auth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Sign up button
        binding.signupBtn.setOnClickListener {
            val email = binding.emailInput.text.toString().trim()
            val password = binding.passwordInput.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                binding.statusText.text = "Email and password cannot be empty"
                return@setOnClickListener
            }

            // Firebase signup
            auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    binding.statusText.text = "Signup successful!"

                    findNavController().navigate(R.id.action_signupFragment_to_homeFragment)
                }
                .addOnFailureListener { e ->
                    binding.statusText.text = "Signup failed: ${e.localizedMessage}"
                }
        }


        binding.goToLoginBtn.setOnClickListener {
            findNavController().navigate(R.id.action_signupFragment_to_loginFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

