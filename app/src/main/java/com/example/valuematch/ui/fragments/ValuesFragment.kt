package com.example.valuematch.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.valuematch.R
import com.example.valuematch.data.models.User
import com.example.valuematch.databinding.FragmentValuesBinding
import com.example.valuematch.repository.UserRepository
import com.example.valuematch.utils.uidOrThrow
import com.google.firebase.firestore.ktx.toObject

class ValuesFragment : Fragment(R.layout.fragment_values) {

    private var _binding: FragmentValuesBinding? = null
    private val binding get() = _binding!!

    private val userRepo = UserRepository()

    private val valuePairs: List<Pair<CheckBox, String>> by lazy {
        listOf(
            binding.valueHonesty to "Honesty",
            binding.valueFamily to "Family",
            binding.valueFaith to "Faith",
            binding.valueLove to "Love",
            binding.valueGaming to "Gaming",
            binding.valuePolitics to "Politics",
            binding.valueMusic to "Music",
            binding.valueMovies to "Movies",
            binding.valueSports to "Sports",
            binding.valueReligion to "Religion",
            binding.valueLeadership to "Leadership",
            binding.valueGrowth to "Growth",
            binding.valueCreativity to "Creativity",
            binding.valueCareer to "Career",
            binding.valueTravel to "Travel"
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentValuesBinding.bind(view)

        val uid = uidOrThrow()

        userRepo.getUser(uid)
            .addOnSuccessListener { snap ->
                val u = snap.toObject<User>()
                val values = u?.values ?: emptyList()
                valuePairs.forEach { (cb, label) ->
                    cb.isChecked = label in values
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    requireContext(),
                    e.message ?: "Failed to load values",
                    Toast.LENGTH_SHORT
                ).show()
            }

        binding.saveValuesButton.setOnClickListener {
            val selected = valuePairs
                .filter { (cb, _) -> cb.isChecked }
                .map { (_, label) -> label }

            if (selected.isEmpty()) {
                Toast.makeText(requireContext(), "Pick at least 1 value", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val update = User(userId = uid, values = selected)

            userRepo.upsertUser(update)
                .addOnSuccessListener {
                    Toast.makeText(requireContext(), "Values saved!", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.swipeFragment)
                }
                .addOnFailureListener { e ->
                    Toast.makeText(requireContext(), e.message ?: "Save failed", Toast.LENGTH_SHORT).show()
                }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}