package com.example.valuematch.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.valuematch.screens.ChatScreen
import com.example.valuematch.screens.HomeScreen
import com.example.valuematch.screens.LoginScreen
import com.example.valuematch.screens.MatchScreen
import com.example.valuematch.screens.ProfileScreen
import com.example.valuematch.screens.SignupScreen

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "login"
    ) {

        composable("login") { LoginScreen(navController) }
        composable("signup") { SignupScreen(navController) }

        composable("home") { HomeScreen(navController) }
        composable("profile") { ProfileScreen(navController) }
        composable("match") { MatchScreen(navController) }
        composable("chat") { ChatScreen(navController) }
    }
}