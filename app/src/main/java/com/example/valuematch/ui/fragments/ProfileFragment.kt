package com.example.valuematch.ui.fragments

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.valuematch.databinding.FragmentProfileBinding
import com.example.valuematch.R
import java.io.File
import java.io.FileOutputStream

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private var imageUri: Uri? = null

    private val selectImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null) {
                imageUri = uri
                binding.profileImageView.setImageURI(uri)
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadProfile()

        binding.selectImageButton.setOnClickListener {
            selectImageLauncher.launch("image/*")
        }

        binding.skipImageButton.setOnClickListener {
            imageUri = null
            saveProfile()
        }

        binding.saveProfileButton.setOnClickListener {
            saveProfile()
        }
    }

    private fun saveProfile() {
        val name = binding.nameEditText.text.toString()
        val age = binding.ageEditText.text.toString()
        val about = binding.aboutEditText.text.toString()

        val prefs = requireContext().getSharedPreferences("user_profile", Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putString("name", name)
        editor.putString("age", age)
        editor.putString("about", about)

        imageUri?.let { _ ->
            val bitmap = binding.profileImageView.drawable.toBitmap()
            val file = File(requireContext().filesDir, "profile_image.png")
            FileOutputStream(file).use { out ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            }
            editor.putString("profile_image_path", file.absolutePath)
        }

        editor.apply()
        Toast.makeText(requireContext(), "Profile saved!", Toast.LENGTH_SHORT).show()


        findNavController().navigate(R.id.action_profileFragment_to_homeFragment)
    }

    private fun loadProfile() {
        val prefs = requireContext().getSharedPreferences("user_profile", Context.MODE_PRIVATE)
        binding.nameEditText.setText(prefs.getString("name", ""))
        binding.ageEditText.setText(prefs.getString("age", ""))
        binding.aboutEditText.setText(prefs.getString("about", ""))

        val imagePath = prefs.getString("profile_image_path", null)
        imagePath?.let {
            val file = File(it)
            if (file.exists()) {
                binding.profileImageView.setImageURI(Uri.fromFile(file))
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
