package com.example.mobile_tarabahoapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.mobile_tarabahoapp.ui.theme.TarabahoTheme
import java.text.SimpleDateFormat
import java.util.*

enum class JobStatus {
    PENDING_ACCEPTANCE,
    UPCOMING,
    IN_PROGRESS,
    COMPLETED
}

data class Job(
    val id: String,
    val title: String,
    val clientName: String,
    val clientImage: Int,
    val location: String,
    val dateTime: Date,
    val payment: Double,
    val status: JobStatus,
    val description: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkerHomeScreen(navController: NavController) {
    val dateFormat = SimpleDateFormat("EEE, MMM dd • h:mm a", Locale.getDefault())

    // Sample job data
    val jobs = remember {
        listOf(
            Job(
                id = "1",
                title = "Garden Maintenance",
                clientName = "Maria Rodriguez",
                clientImage = R.drawable.client_maria,
                location = "123 Palm Avenue, Cebu City",
                dateTime = Calendar.getInstance().apply {
                    add(Calendar.DAY_OF_MONTH, 1)
                    set(Calendar.HOUR_OF_DAY, 10)
                    set(Calendar.MINUTE, 0)
                }.time,
                payment = 350.0,
                status = JobStatus.PENDING_ACCEPTANCE,
                description = "Need help with garden maintenance including trimming hedges, mowing lawn, and removing weeds."
            ),
            Job(
                id = "2",
                title = "Lawn Mowing",
                clientName = "John Smith",
                clientImage = R.drawable.client_john,
                location = "45 Mango Street, Mandaue City",
                dateTime = Calendar.getInstance().apply {
                    add(Calendar.DAY_OF_MONTH, 2)
                    set(Calendar.HOUR_OF_DAY, 14)
                    set(Calendar.MINUTE, 30)
                }.time,
                payment = 250.0,
                status = JobStatus.PENDING_ACCEPTANCE,
                description = "Need lawn mowing service for front and backyard. Approximately 200 sq meters total."
            ),
            Job(
                id = "3",
                title = "Plant Watering",
                clientName = "Emily Chen",
                clientImage = R.drawable.client_emily,
                location = "78 Acacia Road, Lapu-Lapu City",
                dateTime = Calendar.getInstance().apply {
                    add(Calendar.HOUR_OF_DAY, 3)
                }.time,
                payment = 150.0,
                status = JobStatus.UPCOMING,
                description = "Need someone to water indoor and outdoor plants while I'm away for the day."
            ),
            Job(
                id = "4",
                title = "Garden Cleanup",
                clientName = "David Wilson",
                clientImage = R.drawable.client_david,
                location = "22 Orchid Street, Talisay City",
                dateTime = Calendar.getInstance().time,
                payment = 400.0,
                status = JobStatus.IN_PROGRESS,
                description = "Complete garden cleanup including removing fallen leaves, trimming overgrown plants, and organizing garden tools."
            )
        )
    }

    val pendingJobs = jobs.filter { it.status == JobStatus.PENDING_ACCEPTANCE }
    val upcomingJobs = jobs.filter { it.status == JobStatus.UPCOMING }
    val inProgressJobs = jobs.filter { it.status == JobStatus.IN_PROGRESS }

    Scaffold(
        topBar = {
            // Top App Bar with profile and notification icons
            TopAppBar(
                title = {
                    Text(
                        text = "TARABAHO!",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF2962FF),
                    titleContentColor = Color.White
                ),
                actions = {
                    IconButton(onClick = { /* Handle notifications */ }) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notifications",
                            tint = Color.White
                        )
                    }
                    IconButton(onClick = {  navController.navigate("worker_edit_profile")}) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Profile",
                            tint = Color.White
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF5F5F5))
        ) {
            // Worker welcome section
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF2962FF)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Worker avatar
                        Image(
                            painter = painterResource(id = R.drawable.worker_avatar),
                            contentDescription = "Worker Avatar",
                            modifier = Modifier
                                .size(60.dp)
                                .clip(CircleShape)
                                .border(2.dp, Color.White, CircleShape),
                            contentScale = ContentScale.Crop
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        Column {
                            Text(
                                text = "Welcome back, Andre!",
                                color = Color.White,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                text = "You have ${pendingJobs.size} new job requests",
                                color = Color.White,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }

            // Job statistics
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    StatCard(
                        icon = Icons.Default.CheckCircle,
                        value = "12",
                        label = "Completed",
                        color = Color(0xFF4CAF50),
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    StatCard(
                        icon = Icons.Default.Star,
                        value = "4.8",
                        label = "Rating",
                        color = Color(0xFFFFC107),
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    StatCard(
                        icon = Icons.Default.MonetizationOn,
                        value = "₱5,240",
                        label = "Earned",
                        color = Color(0xFF2962FF),
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Pending acceptance jobs section
            if (pendingJobs.isNotEmpty()) {
                item {
                    SectionHeader(title = "New Job Requests", count = pendingJobs.size)
                }

                items(pendingJobs) { job ->
                    JobCard(
                        job = job,
                        dateFormat = dateFormat,
                        onAccept = { /* Handle job acceptance */ },
                        onDecline = { /* Handle job decline */ },
                        onViewDetails = { /* Navigate to job details */ }
                    )
                }
            }

            // Upcoming jobs section
            if (upcomingJobs.isNotEmpty()) {
                item {
                    SectionHeader(title = "Upcoming Jobs", count = upcomingJobs.size)
                }

                items(upcomingJobs) { job ->
                    JobCard(
                        job = job,
                        dateFormat = dateFormat,
                        onViewDetails = { /* Navigate to job details */ }
                    )
                }
            }

            // In progress jobs section
            if (inProgressJobs.isNotEmpty()) {
                item {
                    SectionHeader(title = "In Progress", count = inProgressJobs.size)
                }

                items(inProgressJobs) { job ->
                    JobCard(
                        job = job,
                        dateFormat = dateFormat,
                        onComplete = { /* Handle job completion */ },
                        onViewDetails = { /* Navigate to job details */ }
                    )
                }
            }

            // Bottom padding
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun SectionHeader(title: String, count: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "$count jobs",
            fontSize = 14.sp,
            color = Color.Gray
        )
    }
}

@Composable
fun StatCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    value: String,
    label: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = value,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = label,
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun JobCard(
    job: Job,
    dateFormat: SimpleDateFormat,
    onAccept: (() -> Unit)? = null,
    onDecline: (() -> Unit)? = null,
    onComplete: (() -> Unit)? = null,
    onViewDetails: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable(onClick = onViewDetails),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Job status chip
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Job title
                Text(
                    text = job.title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                // Status chip
                val (statusColor, statusText) = when (job.status) {
                    JobStatus.PENDING_ACCEPTANCE -> Color(0xFFFFA000) to "New Request"
                    JobStatus.UPCOMING -> Color(0xFF2962FF) to "Upcoming"
                    JobStatus.IN_PROGRESS -> Color(0xFF7B1FA2) to "In Progress"
                    JobStatus.COMPLETED -> Color(0xFF388E3C) to "Completed"
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(statusColor.copy(alpha = 0.1f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = statusText,
                        color = statusColor,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Client info
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = job.clientImage),
                    contentDescription = "Client Image",
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = job.clientName,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier.size(14.dp)
                        )

                        Spacer(modifier = Modifier.width(4.dp))

                        Text(
                            text = job.location,
                            fontSize = 14.sp,
                            color = Color.Gray,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Job details
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Date and time
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Schedule,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(16.dp)
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = dateFormat.format(job.dateTime),
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }

                // Payment
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Payments,
                        contentDescription = null,
                        tint = Color(0xFF2962FF),
                        modifier = Modifier.size(16.dp)
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = "₱${job.payment.toInt()}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2962FF)
                    )
                }
            }

            // Job description
            if (job.description.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = job.description,
                    fontSize = 14.sp,
                    color = Color.DarkGray,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Action buttons
            if (onAccept != null || onDecline != null || onComplete != null) {
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    // Decline button (for pending jobs)
                    if (onDecline != null) {
                        OutlinedButton(
                            onClick = onDecline,
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = Color.Gray
                            ),
                            border = ButtonDefaults.outlinedButtonBorder.copy(
                                brush = SolidColor(Color.LightGray)
                            )
                        ) {
                            Text("Decline")
                        }

                        Spacer(modifier = Modifier.width(8.dp))
                    }

                    // Accept button (for pending jobs)
                    if (onAccept != null) {
                        Button(
                            onClick = onAccept,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF2962FF)
                            )
                        ) {
                            Text("Accept")
                        }
                    }

                    // Complete button (for in-progress jobs)
                    if (onComplete != null) {
                        Button(
                            onClick = onComplete,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF388E3C)
                            )
                        ) {
                            Text("Mark Complete")
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WorkerHomeScreenPreview() {
    TarabahoTheme {
        WorkerHomeScreen(rememberNavController())
    }
}