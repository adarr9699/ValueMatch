package com.example.valuematch.ui.fragments

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.valuematch.R
import com.example.valuematch.databinding.FragmentProfileBinding
import com.example.valuematch.utils.uidOrThrow
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID



class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val db = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()

    private var pickedUri: Uri? = null

    private val pickImage = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            pickedUri = uri
            Glide.with(this).load(uri).circleCrop().into(binding.profilePhoto)
            uploadAndSavePhoto(uri)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentProfileBinding.bind(view)

        loadProfile()

        binding.btnEditProfile.setOnClickListener { pickImage.launch("image/*") }
        binding.btnEditProfile.setOnClickListener { pickImage.launch("image/*") }
        binding.btnEditProfile.setOnClickListener { pickImage.launch("image/*") }
    }

    private fun loadProfile() {
        val uid = uidOrThrow()
        db.collection("users").document(uid).get()
            .addOnSuccessListener { doc ->
                val name = doc.getString("name").orEmpty().ifBlank { "Lizzy" }
               // val about = doc.getString("about").orEmpty().ifBlank { "Complete your profile to be seen by more people!" }
                val photoUrl = doc.getString("photoUrl").orEmpty()

                binding.profileNameAge.text = name


                if (photoUrl.isNotBlank()) {
                    Glide.with(this).load(photoUrl).circleCrop().into(binding.profilePhoto)
                } else {
                    binding.profilePhoto.setImageResource(android.R.drawable.sym_def_app_icon)
                }
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to load profile", Toast.LENGTH_SHORT).show()
            }
    }

    private fun uploadAndSavePhoto(uri: Uri) {
        val uid = uidOrThrow()
        val ref = storage.reference.child("profilePhotos/$uid/${UUID.randomUUID()}.jpg")

        ref.putFile(uri)
            .addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener { download ->
                    db.collection("users").document(uid)
                        .set(mapOf("photoUrl" to download.toString()), com.google.firebase.firestore.SetOptions.merge())
                        .addOnSuccessListener {
                            Toast.makeText(requireContext(), "Photo saved", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(requireContext(), "Failed saving photo", Toast.LENGTH_SHORT).show()
                        }
                }
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Upload failed", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}