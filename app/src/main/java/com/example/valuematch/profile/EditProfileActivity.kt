package com.example.valuematch.profile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.example.valuematch.R
import com.example.valuematch.models.UserProfile
import com.example.valuematch.utils.FirebaseUtils

class EditProfileActivity : AppCompatActivity() {

    private var photoUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        val name = findViewById<EditText>(R.id.nameInput)
        val age = findViewById<EditText>(R.id.ageInput)
        val bio = findViewById<EditText>(R.id.bioInput)
        val political = findViewById<EditText>(R.id.politicalInput)
        val religion = findViewById<EditText>(R.id.religionInput)
        val lifestyle = findViewById<EditText>(R.id.lifestyleInput)
        val photoBtn = findViewById<Button>(R.id.uploadPhotoBtn)
        val saveBtn = findViewById<Button>(R.id.saveProfileBtn)

        photoBtn.setOnClickListener { pickPhoto() }

        saveBtn.setOnClickListener {
            uploadProfile(
                name.text.toString(),
                age.text.toString().toInt(),
                bio.text.toString(),
                political.text.toString(),
                religion.text.toString(),
                lifestyle.text.toString()
            )
        }
    }

    private fun pickPhoto() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 1001)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1001 && resultCode == Activity.RESULT_OK) {
            photoUri = data?.data
        }
    }

    private fun uploadProfile(
        name: String, age: Int, bio: String,
        political: String, religious: String, lifestyle: String
    ) {
        val uid = FirebaseUtils.auth.uid ?: return
        val photoRef = FirebaseUtils.storage.child("profile_photos/$uid.jpg")

        if (photoUri != null) {
            photoRef.putFile(photoUri!!)
                .addOnSuccessListener {
                    photoRef.downloadUrl.addOnSuccessListener { url ->

                        val profile = UserProfile(
                            uid, name, age, bio,
                            political, religious, lifestyle,
                            photoUrl = url.toString()
                        )

                        FirebaseUtils.db.collection("users")
                            .document(uid)
                            .set(profile)
                            .addOnSuccessListener {
                                Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT).show()
                                finish()
                            }
                    }
                }
        }
    }
}