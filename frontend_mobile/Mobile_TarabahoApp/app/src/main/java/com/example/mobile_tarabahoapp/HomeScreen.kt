package com.example.mobile_tarabahoapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items


import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.mobile_tarabahoapp.AppNavigation
import com.example.mobile_tarabahoapp.AuthRepository.WorkerViewModel

import com.example.mobile_tarabahoapp.ui.theme.TarabahoTheme



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {

    val workerViewModel: WorkerViewModel = viewModel() // 1️⃣ declare ViewModel FIRST

    val workers by workerViewModel.workers.observeAsState(emptyList())
    var searchQuery by remember { mutableStateOf("Gardener") }
    var selectedCategoryIndex by remember { mutableStateOf(1) } // Gardener selected by default


    LaunchedEffect(Unit) {
        workerViewModel.fetchWorkers()
    }

    val categories = listOf(
        "All" to R.drawable.ic_work,
        "Cleaning" to R.drawable.ic_work,
        "Gardening" to R.drawable.ic_gardener,
        "Errands" to R.drawable.ic_errands,
        "Tutoring" to R.drawable.ic_work,
        "Babysitting" to R.drawable.ic_caretaker
    )

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                contentColor = Color(0xFF2962FF)
            ) {
                NavigationBarItem(
                    icon = { Icon(Icons.Outlined.Notifications, contentDescription = "Notifications") },
                    selected = false,
                    onClick = { /* Handle navigation */ },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = Color.White,
                        selectedIconColor = Color(0xFF2962FF),
                        unselectedIconColor = Color.Gray
                    )
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Outlined.Person, contentDescription = "Settings") },
                    selected = false, // You can update this based on currentDestination if needed
                    onClick = {
                        navController.navigate("settings") {

                        }
                    },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = Color.White,
                        selectedIconColor = Color(0xFF2962FF),
                        unselectedIconColor = Color.Gray
                    )
                )

                NavigationBarItem(
                    icon = { Icon(Icons.Outlined.Home, contentDescription = "Home") },
                    selected = true,
                    onClick = { /* Handle navigation */ },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = Color.White,
                        selectedIconColor = Color(0xFF2962FF),
                        unselectedIconColor = Color.Gray
                    )
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Outlined.Description, contentDescription = "Tasks") },
                    selected = false,
                    onClick = { /* Handle navigation */ },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = Color.White,
                        selectedIconColor = Color(0xFF2962FF),
                        unselectedIconColor = Color.Gray
                    )
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Outlined.Help, contentDescription = "Help") },
                    selected = false,
                    onClick = { /* Handle navigation */ },
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
        ) {
            // Header with app name
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF2962FF))
                    .padding(top = 16.dp, bottom = 24.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Text(
                        text = "TARABAHO !",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // Search bar
                    TextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.White),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            disabledContainerColor = Color.White,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        placeholder = { Text("Search") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search",
                                tint = Color.Gray
                            )
                        },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { searchQuery = "" }) {
                                    Icon(
                                        imageVector = Icons.Default.Clear,
                                        contentDescription = "Clear search",
                                        tint = Color.Gray
                                    )
                                }
                            }
                        },
                        singleLine = true
                    )
                }
            }

            // Categories section
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF29B6F6)
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Categories",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        categories.forEachIndexed { index, (name, iconRes) ->
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable {
                                        selectedCategoryIndex = index
                                        if (name == "All") {
                                            workerViewModel.fetchWorkers()
                                        } else {
                                            workerViewModel.fetchWorkersByCategory(name)
                                        }
                                    }

                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(48.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color.White)
                                        .border(
                                            width = if (selectedCategoryIndex == index) 2.dp else 0.dp,
                                            color = if (selectedCategoryIndex == index) Color(0xFF2962FF) else Color.Transparent,
                                            shape = RoundedCornerShape(8.dp)
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        painter = painterResource(id = iconRes),
                                        contentDescription = name,
                                        tint = if (selectedCategoryIndex == index) Color(0xFF2962FF) else Color.Gray,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = name,
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    textAlign = TextAlign.Center,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }

            // Results count and filter
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${workers.size} found",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )


                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Default",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Sort",
                        tint = Color.Gray
                    )
                }
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(workers) { worker ->
                    WorkerCard(worker = worker) {
                        navController.navigate("worker_details/${worker.id}")
                    }
                }
                // Add some bottom padding
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    TarabahoTheme {
        val navController = rememberNavController()
        HomeScreen(navController = navController)
    }
}