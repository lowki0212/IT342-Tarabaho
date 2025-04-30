package com.example.mobile_tarabahoapp

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "signup") {
        composable(route = "signup") {
            SignUpScreen(
                onSignUpSuccess = {
                    navController.popBackStack()
                    navController.navigate("login") // or "home" if that's your next screen
                }
            )
        }
        composable("worker_details/{workerId}") { backStackEntry ->
            val workerId = backStackEntry.arguments?.getString("workerId") ?: return@composable
            WorkerDetailsScreen(navController = navController, workerId = workerId)
        }
        composable("login") {
            LoginScreen(navController)
        }
        composable("home") {
            HomeScreen(navController)
        }

        composable("settings") {
            SettingsScreen(navController)
        }

        composable(route = "profilesettings") {
            EditProfileScreen(navController = navController)
        }

        composable("search_results") {
            SearchResultsScreen(navController)
        }

        composable("book_appointment/{workerId}") { backStackEntry ->
            val workerId = backStackEntry.arguments?.getString("workerId")
            BookAppointmentScreen(navController)
        }

        composable("change_payment_method") {
            ChangePaymentMethodScreen(navController)
        }

        composable("worker_signin") {
            WorkerSignInScreen(navController)
        }

        composable("worker_home") {
            WorkerHomeScreen(navController)
        }

        composable("worker_edit_profile") {
            WorkerEditProfileScreen(navController = navController)
        }
        // Add more screens here later, like:
        // composable("home") { HomeScreen() }
    }

}
