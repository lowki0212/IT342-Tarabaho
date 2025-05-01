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
            val workerIdString = backStackEntry.arguments?.getString("workerId") ?: return@composable
            val workerId = workerIdString.toLongOrNull() ?: return@composable
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

        composable(route = "book_appointment/{workerId}") { backStackEntry ->
            val workerId = backStackEntry.arguments?.getString("workerId")?.toLongOrNull() ?: 0L
            BookAppointmentScreen(navController = navController, workerId = workerId)
        }

        composable("booking_status/{bookingId}") { backStackEntry ->
            val bookingId = backStackEntry.arguments?.getString("bookingId") ?: ""
            BookingStatusScreen(navController, bookingId = bookingId)
        }

        composable("booking_details/{bookingId}") { backStackEntry ->
            val bookingId = backStackEntry.arguments?.getString("bookingId") ?: ""
            BookingDetailsScreen(navController, bookingId = bookingId)
        }
        composable("booking_status_rejected") {
            BookingStatusScreen(
                navController = navController,
                initialStatus = BookingStatusState.REJECTED
            )
        }

        composable("booking_details/{bookingId}") { backStackEntry ->
            val bookingId = backStackEntry.arguments?.getString("bookingId") ?: ""
            BookingDetailsScreen(navController, bookingId = bookingId)
        }

        composable("rate_worker/{bookingId}") { backStackEntry ->
            val bookingId = backStackEntry.arguments?.getString("bookingId")?.toLongOrNull() ?: 0L
            RateWorkerScreen(navController = navController, bookingId = bookingId)
        }

        composable("worker_booking_details/{bookingId}") { backStackEntry ->
            val bookingId = backStackEntry.arguments?.getString("bookingId")?.toLongOrNull() ?: return@composable
            WorkerBookingDetailsScreen(navController = navController, bookingId = bookingId)
        }
        // Add more screens here later, like:
        // composable("home") { HomeScreen() }
    }

}
