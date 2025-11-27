package com.example.valuematch.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.valuematch.repository.AuthRepository

@Composable
fun LoginScreen(navController: NavController) {
    val repo = AuthRepository()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(24.dp)) {

        Text("Login", style = MaterialTheme.typography.headlineMedium)

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
            repo.login(email, password) { success, message ->
                if (success) {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                } else error = message ?: "Login failed"
            }
        }) {
            Text("Login")
        }

        if (error.isNotEmpty())
            Text(error, color = MaterialTheme.colorScheme.error)

        Spacer(Modifier.height(8.dp))

        TextButton(onClick = { navController.navigate("signup") }) {
            Text("Create an account")
        }
    }
}