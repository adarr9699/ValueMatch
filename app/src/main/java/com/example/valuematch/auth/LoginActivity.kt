package com.example.valuematch.auth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.valuematch.R
import com.example.valuematch.utils.FirebaseUtils


class LoginActivity : AppCompatActivity() {
    override fun onCreate(saveInstanceState: Bundle?) {
        super.onCreate(saveInstanceState)
        setContentView(R.layout.activity_login)

        val email = findViewById<EditText>(R.id.emailInput)
        val password = findViewById<EditText>(R.id.passwordInput)
        val loginBtn = findViewById<Button>(R.id.loginBtn)
        val registerBtn = findViewById<Button>(R.id.goToRegisterBtn)

        loginBtn.setOnClickListener {
            FirebaseUtils.auth.signInWithEmailAndPassword(
                email.text.toString(),
                password.text.toString()
            ).addOnSuccessListener {
                startActivity(Intent(this, MatchActivity::class.java))
            }.addOnFailureListener {
                Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
            }
        }

        registerBtn.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}