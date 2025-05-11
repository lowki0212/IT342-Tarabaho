package com.example.mobile_tarabahoapp

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mobile_tarabahoapp.AuthRepository.BookingViewModel
import com.example.mobile_tarabahoapp.ui.theme.TarabahoTheme

data class BookingDetail(
    val id: String,
    val category: String,
    val status: String,
    val paymentMethod: String,
    val jobDetails: String,
    val createdAt: String,
    val updatedAt: String,
    val type: String,
    val worker: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingDetailsScreen(navController: NavController, bookingId: String) {

    val viewModel: BookingViewModel = viewModel()
    val booking by viewModel.selectedBooking.observeAsState()

    // Fetch booking from backend
    LaunchedEffect(bookingId) {
        viewModel.getBookingById(bookingId.toLong())
    }
    // Sample booking data
    val bookingDetail = remember(booking) {
        BookingDetail(
            id = booking?.id?.toString() ?: "",
            category = booking?.category?.name ?: "Unknown",
            status = booking?.status ?: "PENDING",
            paymentMethod = booking?.paymentMethod ?: "Unknown",
            jobDetails = booking?.jobDetails ?: "No job details",
            createdAt = booking?.createdAt ?: "",
            updatedAt = booking?.updatedAt ?: "",
            type = booking?.type ?: "",
            worker = "${booking?.worker?.firstName ?: "Unknown"} ${booking?.worker?.lastName ?: ""}"
        )
    }
    // State for job completion dialog
    var showCompletionDialog by remember { mutableStateOf(false) }

    // State for job started
    var jobStarted by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Booking Details",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF2962FF)
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                contentColor = Color(0xFF2962FF)
            ) {

                NavigationBarItem(
                    icon = { Icon(Icons.Outlined.Person, contentDescription = "Profile") },
                    selected = false,
                    onClick = { navController.navigate("settings") },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = Color.White,
                        selectedIconColor = Color(0xFF2962FF),
                        unselectedIconColor = Color.Gray
                    )
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Outlined.Home, contentDescription = "Home") },
                    selected = true,
                    onClick = { navController.navigateUp() },
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
            // Status Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = when (bookingDetail.status) {
                        "ACCEPTED" -> Color(0xFFE8F5E9)
                        "IN_PROGRESS" -> Color(0xFFE3F2FD)
                        "COMPLETED" -> Color(0xFFE0F7FA)
                        else -> Color(0xFFFFF3E0)
                    }
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = when (bookingDetail.status) {
                            "ACCEPTED" -> Icons.Default.CheckCircle
                            "IN_PROGRESS" -> Icons.Default.Pending
                            "COMPLETED" -> Icons.Default.Done
                            else -> Icons.Default.Schedule
                        },
                        contentDescription = null,
                        tint = when (bookingDetail.status) {
                            "ACCEPTED" -> Color(0xFF4CAF50)
                            "IN_PROGRESS" -> Color(0xFF2196F3)
                            "COMPLETED" -> Color(0xFF00BCD4)
                            else -> Color(0xFFFF9800)
                        },
                        modifier = Modifier.size(32.dp)
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Text(
                            text = "Status",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )

                        Text(
                            text = bookingDetail.status,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = when (bookingDetail.status) {
                                "ACCEPTED" -> Color(0xFF4CAF50)
                                "IN_PROGRESS" -> Color(0xFF2196F3)
                                "COMPLETED" -> Color(0xFF00BCD4)
                                else -> Color(0xFFFF9800)
                            }
                        )
                    }
                }
            }

            // Worker Info Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Worker avatar
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE1F5FE))
                            .border(2.dp, Color(0xFF2962FF), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Worker",
                            tint = Color(0xFF2962FF),
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Text(
                            text = "Worker",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )

                        Text(
                            text = bookingDetail.worker,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = Color(0xFFFFC107),
                                modifier = Modifier.size(16.dp)
                            )

                            Spacer(modifier = Modifier.width(4.dp))

                            Text(
                                text = "4.8",
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Booking Details Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Booking Information",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // Booking ID
                    DetailRow(
                        label = "Booking ID",
                        value = bookingDetail.id,
                        icon = Icons.Default.Numbers
                    )

                    Divider(modifier = Modifier.padding(vertical = 8.dp))

                    // Category
                    DetailRow(
                        label = "Category",
                        value = bookingDetail.category,
                        icon = Icons.Default.Category
                    )

                    Divider(modifier = Modifier.padding(vertical = 8.dp))

                    // Payment Method
                    DetailRow(
                        label = "Payment Method",
                        value = bookingDetail.paymentMethod,
                        icon = Icons.Default.Payments
                    )

                    Divider(modifier = Modifier.padding(vertical = 8.dp))

                    // Job Details
                    DetailRow(
                        label = "Job Details",
                        value = bookingDetail.jobDetails,
                        icon = Icons.Default.Description
                    )

                    Divider(modifier = Modifier.padding(vertical = 8.dp))

                    // Type
                    DetailRow(
                        label = "Type",
                        value = bookingDetail.type,
                        icon = Icons.Default.Label
                    )

                    Divider(modifier = Modifier.padding(vertical = 8.dp))

                    // Created At
                    DetailRow(
                        label = "Created At",
                        value = bookingDetail.createdAt,
                        icon = Icons.Default.CalendarToday
                    )

                    Divider(modifier = Modifier.padding(vertical = 8.dp))

                    // Updated At
                    DetailRow(
                        label = "Updated At",
                        value = bookingDetail.updatedAt,
                        icon = Icons.Default.Update
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Action Buttons
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                // Start Button
                Button(
                    onClick = {
                        viewModel.startBooking(bookingId.toLong(),
                            onSuccess = {
                                jobStarted = true
                            },
                            onError = { errorMessage ->
                                Log.e("BookingDetailsScreen", "Failed to start job: $errorMessage")
                            }
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (jobStarted || bookingDetail.status == "IN_PROGRESS") Color.Gray else Color(0xFF2962FF),
                        contentColor = Color.White
                    ),
                    enabled = bookingDetail.status == "ACCEPTED" && !jobStarted
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = null
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = if (jobStarted || bookingDetail.status == "IN_PROGRESS") "Job Started" else "Start Job",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Accept Completion Button
                Button(
                    onClick = { showCompletionDialog = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4CAF50),
                        contentColor = Color.White
                    ),
                    enabled = jobStarted
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "Accept Completion",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    // Job Completion Dialog
    if (showCompletionDialog) {
        AlertDialog(
            onDismissRequest = { showCompletionDialog = false },
            title = { Text("Confirm Job Completion") },
            text = {
                Text(
                    "Are you satisfied with the work done by ${bookingDetail.worker}? " +
                            "Confirming completion will finalize the payment and close this booking."
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showCompletionDialog = false
                        viewModel.acceptCompletion(
                            bookingId = bookingDetail.id.toLong(),
                            onSuccess = {
                                // ✅ Booking marked as completed in backend → go to rate worker screen
                                navController.navigate("rate_worker/${bookingDetail.id}")
                            },
                            onError = { errorMessage ->
                                Log.e("BookingDetailsScreen", "Failed to accept completion: $errorMessage")
                            }
                        )
                    }
                ) {
                    Text(
                        text = "Yes, Complete Job",
                        color = Color(0xFF4CAF50)
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showCompletionDialog = false }
                ) {
                    Text("Not Yet")
                }
            }
        )
    }
}

@Composable
fun DetailRow(
    label: String,
    value: String,
    icon: ImageVector
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color(0xFF2962FF),
            modifier = Modifier.size(20.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column {
            Text(
                text = label,
                fontSize = 14.sp,
                color = Color.Gray
            )

            Text(
                text = value,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BookingDetailsScreenPreview() {
    TarabahoTheme {
        BookingDetailsScreen(
            navController = rememberNavController(),
            bookingId = "BK123"
        )
    }
}