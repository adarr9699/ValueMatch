package com.example.valuematch.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth


@Composable
fun HomeScreen(navController: NavController) {
    val user = FirebaseAuth.getInstance().currentUser
    val displayName = user?.email ?: "User"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {

        // this is the greeting
        Text(
            text = "Welcome, $displayName!",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(Modifier.height(24.dp))

        // this is the main Buttons
        Button(
            onClick = { navController.navigate("match") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Find Matches")
        }

        Spacer(Modifier.height(12.dp))

        Button(
            onClick = { navController.navigate("profile") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Edit Profile")
        }

        Spacer(Modifier.height(12.dp))

        Button(
            onClick = { navController.navigate("chat") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Messages")
        }

        Spacer(Modifier.height(32.dp))

        // this is the user preview section
        Text(
            "Your Core Values",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(Modifier.height(8.dp))

        // this is the placeholder values
        ChipList(items = listOf("Honesty", "Respect", "Ambition"))
    }
}

@Composable
fun ChipList(items: List<String>) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items.forEach { label ->
            AssistChip(
                onClick = {},
                label = { Text(label) }
            )
        }
    }
}