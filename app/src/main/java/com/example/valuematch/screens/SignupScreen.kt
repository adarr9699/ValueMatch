package com.example.valuematch.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.valuematch.repository.AuthRepository

@Composable
fun SignupScreen(navController: NavController) {
    val repo = AuthRepository()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(24.dp)) {

        Text("Sign Up", style = MaterialTheme.typography.headlineMedium)

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") }
        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") }
        )

        Spacer(Modifier.height(16.dp))

        Button(onClick = {
            repo.signup(email, password) { success, message ->
                if (success) {
                    navController.navigate("home") {
                        popUpTo("signup") { inclusive = true }
                    }
                } else error = message ?: "Signup failed"
            }
        }) {
            Text("Create Account")
        }

        if (error.isNotEmpty())
            Text(error, color = MaterialTheme.colorScheme.error)
    }
}
