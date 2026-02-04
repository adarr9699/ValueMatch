package com.example.valuematch.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.valuematch.R
import com.example.valuematch.data.models.User
import com.example.valuematch.databinding.FragmentValuesBinding
import com.example.valuematch.repository.UserRepository
import com.example.valuematch.utils.FirebaseUtils

class ValuesFragment : Fragment(R.layout.fragment_values) {

    private var _binding: FragmentValuesBinding? = null
    private val binding get() = _binding!!

    private val userRepo = UserRepository()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentValuesBinding.bind(view)

        val uid = FirebaseUtils.uidOrThrow()

        userRepo.getUser(uid).addOnSuccessListener { snap ->
            val u = snap.toObject(User::class.java)
            val values = u?.values ?: emptyList()
            binding.valueHonesty.isChecked = "Honesty" in values
            binding.valueFamily.isChecked = "Family" in values
            binding.valueFaith.isChecked = "Faith" in values
            binding.valueLove.isChecked = "Love" in values
            binding.valueGaming.isChecked = "Gaming" in values
            binding.valuePolitics.isChecked = "Politics" in values
            binding.valueMusic.isChecked = "Music" in values
            binding.valueMovies.isChecked = "Movies" in values
            binding.valueSports.isChecked = "Sports" in values
            binding.valueReligion.isChecked = "Religion" in values
            binding.valueLeadership.isChecked = "Leadership" in values
            binding.valueGrowth.isChecked = "Growth" in values
            binding.valueCreativity.isChecked = "Creativity" in values
            binding.valueCareer.isChecked = "Career" in values
            binding.valueTravel.isChecked = "Travel" in values
        }
        binding.saveValuesButton.setOnClickListener {
            val selected = mutableListOf<String>()
            if (binding.valueHonesty.isChecked) selected.add("Honesty")
            if (binding.valueFamily.isChecked) selected.add("Family")
            if (binding.valueFaith.isChecked) selected.add("Faith")
            if (binding.valueGaming.isChecked) selected.add("Gaming")
            if (binding.valuePolitics.isChecked) selected.add("Politics")
            if (binding.valueLove.isChecked) selected.add("Love")
            if(binding.valueMusic.isChecked) selected.add("Music")
            if(binding.valueMovies.isChecked) selected.add("Movies")
            if(binding.valueSports.isChecked) selected.add("Sports")
            if(binding.valueReligion.isChecked) selected.add("Religion")
            if(binding.valueLeadership.isChecked) selected.add("Leadership")
            if(binding.valueGrowth.isChecked) selected.add("Growth")
            if(binding.valueCreativity.isChecked) selected.add("Creativity")
            if(binding.valueCareer.isChecked) selected.add("Career")
            if(binding.valueTravel.isChecked) selected.add("Travel")

            if (selected.isEmpty()) {
                Toast.makeText(requireContext(), "Pick at least 1 value", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            userRepo.getUser(uid).addOnSuccessListener { snap ->
                val old = snap.toObject(User::class.java) ?: User(userId = uid)
                userRepo.upsertUser(old.copy(values = selected))
                    .addOnSuccessListener {
                        Toast.makeText(requireContext(), "Values saved!", Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.action_valuesFragment_to_swipeFragment)
                    }
                    .addOnFailureListener {
                        Toast.makeText(requireContext(), it.message ?: "Save failed", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}