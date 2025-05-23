package com.example.mobile_tarabahoapp

import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.mobile_tarabahoapp.AuthRepository.BookingViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlinx.coroutines.delay
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkerBookingDetailsScreen(
    navController: NavController,
    bookingId: Long
) {
    val viewModel: BookingViewModel = viewModel()
    val booking by viewModel.selectedBooking.observeAsState()
    LaunchedEffect(bookingId) {
        while (true) {
            viewModel.getBookingById(bookingId)
            delay(3000) // every 3 seconds
        }
    }
    // For confirmation dialog
    var showConfirmDialog by remember { mutableStateOf(false) }
    var confirmAction by remember { mutableStateOf<() -> Unit>({}) }
    var confirmTitle by remember { mutableStateOf("") }
    var confirmMessage by remember { mutableStateOf("") }
    var confirmButtonText by remember { mutableStateOf("") }
    var confirmButtonColor by remember { mutableStateOf(Color(0xFF2962FF)) }

    LaunchedEffect(bookingId) {
        viewModel.getBookingById(bookingId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Booking Details",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF2962FF)),

                )
        }
    ) { paddingValues ->
        if (booking == null) {
            // Loading state
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(color = Color(0xFF2962FF))

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Loading booking details...",
                        color = Color.Gray,
                        fontSize = 16.sp
                    )
                }
            }
        } else {
            val bookingDetail = booking!!
            val status = bookingDetail.status
            val canAcceptOrReject = status == "PENDING"
            val canStart = status == "ACCEPTED"
            val canComplete = status == "IN_PROGRESS"
            val canChat = status == "ACCEPTED" || status == "IN_PROGRESS" || status == "COMPLETED"

            // Format dates
            val dateFormat = SimpleDateFormat("MMM dd, yyyy, hh:mm a", Locale.getDefault())
            val createdAtFormatted = try {
                dateFormat.format(bookingDetail.createdAt)
            } catch (e: Exception) {
                bookingDetail.createdAt.toString()
            }

            val updatedAtFormatted = bookingDetail.updatedAt?.let {
                try {
                    dateFormat.format(it)
                } catch (e: Exception) {
                    it.toString()
                }
            } ?: "N/A"

            // Status color
            val statusColor = when (status) {
                "PENDING" -> Color(0xFFFFA000)
                "ACCEPTED" -> Color(0xFF2962FF)
                "IN_PROGRESS" -> Color(0xFF7B1FA2)
                "COMPLETED" -> Color(0xFF388E3C)
                "REJECTED" -> Color(0xFFD32F2F)
                else -> Color.Gray
            }

            val statusBgColor = when (status) {
                "PENDING" -> Color(0xFFFFF3E0)
                "ACCEPTED" -> Color(0xFFE3F2FD)
                "IN_PROGRESS" -> Color(0xFFF3E5F5)
                "COMPLETED" -> Color(0xFFE8F5E9)
                "REJECTED" -> Color(0xFFFFEBEE)
                else -> Color(0xFFF5F5F5)
            }

            val statusIcon = when (status) {
                "PENDING" -> Icons.Default.Schedule
                "ACCEPTED" -> Icons.Default.CheckCircle
                "IN_PROGRESS" -> Icons.Default.Pending
                "COMPLETED" -> Icons.Default.Done
                "REJECTED" -> Icons.Default.Cancel
                else -> Icons.Default.Info
            }

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
                    colors = CardDefaults.cardColors(containerColor = statusBgColor),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = statusIcon,
                            contentDescription = null,
                            tint = statusColor,
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
                                text = status,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = statusColor
                            )
                        }
                    }
                }

                // Client Info Card
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
                        // Client avatar
                        Box(
                            modifier = Modifier
                                .size(60.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFE1F5FE)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Client",
                                tint = Color(0xFF2962FF),
                                modifier = Modifier.size(32.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column {
                            Text(
                                text = "Client",
                                fontSize = 14.sp,
                                color = Color.Gray
                            )

                            Text(
                                text = "${bookingDetail.user?.firstname ?: ""} ${bookingDetail.user?.lastname ?: ""}",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Category,
                                    contentDescription = null,
                                    tint = Color(0xFF2962FF),
                                    modifier = Modifier.size(16.dp)
                                )

                                Spacer(modifier = Modifier.width(4.dp))

                                Text(
                                    text = bookingDetail.category.name,
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
                        BookingDetailRow(
                            label = "Booking ID",
                            value = "#${bookingDetail.id}",
                            icon = Icons.Default.Numbers
                        )

                        Divider(modifier = Modifier.padding(vertical = 8.dp))

                        // Job Details
                        BookingDetailRow(
                            label = "Job Details",
                            value = bookingDetail.jobDetails,
                            icon = Icons.Default.Description
                        )

                        Divider(modifier = Modifier.padding(vertical = 8.dp))

                        // Payment Method
                        BookingDetailRow(
                            label = "Payment Method",
                            value = bookingDetail.paymentMethod,
                            icon = Icons.Default.Payments
                        )

                        Divider(modifier = Modifier.padding(vertical = 8.dp))

                        // Type
                        BookingDetailRow(
                            label = "Type",
                            value = bookingDetail.type,
                            icon = Icons.Default.Label
                        )

                        Divider(modifier = Modifier.padding(vertical = 8.dp))

                        // Created At
                        BookingDetailRow(
                            label = "Created At",
                            value = createdAtFormatted,
                            icon = Icons.Default.CalendarToday
                        )

                        Divider(modifier = Modifier.padding(vertical = 8.dp))

                        // Updated At
                        BookingDetailRow(
                            label = "Updated At",
                            value = updatedAtFormatted,
                            icon = Icons.Default.Update
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Action Buttons based on status
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    // Chat button - Only visible for ACCEPTED, IN_PROGRESS, or COMPLETED status
                    if (canChat) {
                        Button(
                            onClick = {
                                // Navigate to chat with client
                                navController.navigate("chat/$bookingId")
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF2196F3)
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Chat,
                                contentDescription = "Chat"
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Text(
                                text = "Chat with Client",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    if (canAcceptOrReject) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Reject Button
                            OutlinedButton(
                                onClick = {
                                    confirmTitle = "Reject Booking"
                                    confirmMessage = "Are you sure you want to reject this booking? This action cannot be undone."
                                    confirmButtonText = "Yes, Reject"
                                    confirmButtonColor = Color(0xFFD32F2F)
                                    confirmAction = {
                                        viewModel.rejectBooking(bookingDetail.id)
                                        navController.navigateUp()
                                    }
                                    showConfirmDialog = true
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(56.dp),
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = Color(0xFFD32F2F)
                                ),
                                border = BorderStroke(1.dp, Color(0xFFD32F2F))
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = null
                                )

                                Spacer(modifier = Modifier.width(8.dp))

                                Text(
                                    text = "Reject",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            // Accept Button
                            Button(
                                onClick = {
                                    confirmTitle = "Accept Booking"
                                    confirmMessage = "Are you sure you want to accept this booking? You'll be responsible for completing this job."
                                    confirmButtonText = "Yes, Accept"
                                    confirmButtonColor = Color(0xFF2962FF)
                                    confirmAction = {
                                        viewModel.acceptBooking(bookingDetail.id)
                                        // Refresh the booking details
                                        viewModel.getBookingById(bookingDetail.id)
                                    }
                                    showConfirmDialog = true
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(56.dp),
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF2962FF)
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null
                                )

                                Spacer(modifier = Modifier.width(8.dp))

                                Text(
                                    text = "Accept",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    if (canComplete) {
                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = {
                                confirmTitle = "Complete Job"
                                confirmMessage = "Have you finished all the required work? The client will be asked to confirm completion."
                                confirmButtonText = "Yes, Mark Complete"
                                confirmButtonColor = Color(0xFF388E3C)
                                confirmAction = {
                                    viewModel.completeBooking(bookingDetail.id)
                                    navController.navigateUp() // Go back to home after marking complete
                                }
                                showConfirmDialog = true
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF388E3C)
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Text(
                                text = "Mark Complete",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }

    // Confirmation Dialog
    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            title = { Text(confirmTitle) },
            text = { Text(confirmMessage) },
            confirmButton = {
                TextButton(
                    onClick = {
                        confirmAction()
                        showConfirmDialog = false
                    }
                ) {
                    Text(
                        text = confirmButtonText,
                        color = confirmButtonColor
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showConfirmDialog = false }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun BookingDetailRow(
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