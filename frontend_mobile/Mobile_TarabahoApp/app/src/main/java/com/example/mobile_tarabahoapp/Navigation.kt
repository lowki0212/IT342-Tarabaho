package com.example.mobile_tarabahoapp

import BookingDetailsScreen
import WorkerBookingDetailsScreen
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mobile_tarabahoapp.AuthRepository.BookingViewModel

import com.example.mobile_tarabahoapp.api.RetrofitClient
import com.example.mobile_tarabahoapp.api.chat.ChatRepository
import com.example.mobile_tarabahoapp.utils.TokenManager

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "splash") {

        composable("signup") {
            SignUpScreen(
                navController = navController,
                onSignUpSuccess = {
                    navController.popBackStack()
                    navController.navigate("login")
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

        composable("worker_register") {
            WorkerRegisterScreen(navController)
        }

        composable("user_bookings") {
            val bookingViewModel: BookingViewModel = viewModel()
            UserBookingScreen(navController = navController, viewModel = bookingViewModel)
        }



        // Add more screens here later, like:
        // composable("home") { HomeScreen() }
    }

}
