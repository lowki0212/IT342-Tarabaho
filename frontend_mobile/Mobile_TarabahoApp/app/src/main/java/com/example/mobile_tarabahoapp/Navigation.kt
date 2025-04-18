package com.example.mobile_tarabahoapp

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "signup") {
        composable("signup") {
            SignUpScreen()
        }

        composable("home") {
            HomeScreen()
        }
        // Add more screens here later, like:
        // composable("home") { HomeScreen() }
    }

}
