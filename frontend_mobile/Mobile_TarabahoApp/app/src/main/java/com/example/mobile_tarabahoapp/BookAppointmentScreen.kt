package com.example.mobile_tarabahoapp

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mobile_tarabahoapp.ui.theme.TarabahoTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mobile_tarabahoapp.AuthRepository.BookingViewModel
import com.example.mobile_tarabahoapp.model.PaymentMethod

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookAppointmentScreen(navController: NavController, workerId: Long) {
    // State for job details
    var jobDetails by remember { mutableStateOf("") }
    val viewModel: BookingViewModel = viewModel()
    // State for payment method
    // ✅ FIXED → Replace String list with enum list (PaymentMethod)
    val paymentOptions = listOf(PaymentMethod.CASH, PaymentMethod.GCASH)

    // ✅ FIXED → selectedPaymentOption is now PaymentMethod (not String)
    var selectedPaymentOption by remember { mutableStateOf(paymentOptions[0]) }
    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                contentColor = Color(0xFF2962FF)
            ) {

                NavigationBarItem(
                    icon = { Icon(Icons.Outlined.Person, contentDescription = "Profile") },
                    selected = false,
                    onClick = { navController.navigate("settings")  },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = Color.White,
                        selectedIconColor = Color(0xFF2962FF),
                        unselectedIconColor = Color.Gray
                    )
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Outlined.Home, contentDescription = "Home") },
                    selected = true,
                    onClick = {  navController.navigateUp() },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = Color.White,
                        selectedIconColor = Color(0xFF2962FF),
                        unselectedIconColor = Color.Gray
                    )
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Outlined.Description, contentDescription = "Tasks") },
                    selected = false,
                    onClick = { navController.navigate("user_bookings") },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = Color.White,
                        selectedIconColor = Color(0xFF2962FF),
                        unselectedIconColor = Color.Gray
                    )
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF5F5F5))
                .verticalScroll(rememberScrollState())
        ) {
            // Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF2962FF))
                    .padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { navController.navigateUp() },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Text(
                        text = "Book Appointment",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Job Details Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Job Details",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        OutlinedTextField(
                            value = jobDetails,
                            onValueChange = { jobDetails = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp),
                            label = { Text("Describe your job requirements") },
                            placeholder = { Text("Example: I want CR to be cleaned") },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF2962FF),
                                unfocusedBorderColor = Color.LightGray
                            ),
                            maxLines = 5
                        )
                    }
                }
            }

            // Payment Method Section
            // Payment Method Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "Payment Method",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .selectableGroup()
                    ) {
                        // ✅ Loop through PaymentMethod enum
                        paymentOptions.forEach { option ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp)
                                    .selectable(
                                        selected = selectedPaymentOption == option,
                                        onClick = { selectedPaymentOption = option },
                                        role = Role.RadioButton
                                    )
                                    .padding(horizontal = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = selectedPaymentOption == option,
                                    onClick = null,
                                    colors = RadioButtonDefaults.colors(
                                        selectedColor = Color(0xFF2962FF)
                                    )
                                )

                                Spacer(modifier = Modifier.width(16.dp))

                                // ✅ Icon based on option
                                val icon = when (option) {
                                    PaymentMethod.CASH -> Icons.Default.Money
                                    PaymentMethod.GCASH -> Icons.Default.AccountBalance
                                    else -> Icons.Default.Money
                                }

                                Icon(
                                    imageVector = icon,
                                    contentDescription = option.name,
                                    tint = if (option == PaymentMethod.CASH) Color(0xFF2962FF) else Color(0xFF0070E0),
                                    modifier = Modifier.size(24.dp)
                                )

                                Spacer(modifier = Modifier.width(16.dp))

                                // ✅ Display proper name: Cash / GCash (not all uppercase)
                                Text(
                                    text = option.name.lowercase().replaceFirstChar { it.uppercase() },
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }

                            // ✅ Divider after each option (clean version)
                            Divider(
                                modifier = Modifier.padding(vertical = 8.dp),
                                color = Color.LightGray
                            )
                        }
                    }
                }
            }

            // Confirm Button
            Button(
                onClick = {
                    viewModel.createBooking(
                        workerId = workerId,
                        categoryName = "Cleaning",
                        paymentMethod = selectedPaymentOption,
                        jobDetails = jobDetails,
                        onSuccess = { newBookingId ->
                            if (newBookingId > 0) {
                                // ✅ Valid booking ID, proceed to navigate
                                navController.navigate("booking_status/$newBookingId") {
                                    launchSingleTop = true
                                }
                            } else {
                                // ✅ Invalid booking ID (booking failed or returned null), show error
                                Log.e("BookAppointment", "Booking failed: Invalid Booking ID ($newBookingId)")
                            }
                        },
                        onError = { errorMessage ->
                            Log.e("BookAppointment", "Booking failed: $errorMessage")
                        }
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2962FF)
                )
            ) {
                Text(
                    text = "Confirm Booking",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Add some bottom padding
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BookAppointmentScreenPreview() {
    TarabahoTheme {
        BookAppointmentScreen(rememberNavController(), workerId = 1L)
    }
}