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
import com.example.mobile_tarabahoapp.ui.theme.TarabahoTheme

data class ServiceProvider(
    val id: Int,
    val name: String,
    val profession: String,
    val location: String,
    val rating: Float,
    val reviews: Int,
    val imageRes: Int,
    var isFavorite: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    var searchQuery by remember { mutableStateOf("Gardener") }
    var selectedCategoryIndex by remember { mutableStateOf(1) } // Gardener selected by default

    val categories = listOf(
        "Work" to R.drawable.ic_work,
        "Gardener" to R.drawable.ic_gardener,
        "Errands" to R.drawable.ic_errands,
        "Caretaker" to R.drawable.ic_caretaker
    )

    // Sample data for service providers
    val serviceProviders = remember {
        mutableStateListOf(
            ServiceProvider(
                id = 1,
                name = "Angelo Quieta",
                profession = "Caretaker",
                location = "Siomai Sa Tisa",
                rating = 4f,
                reviews = 1872,
                imageRes = R.drawable.profile_angelo,
                isFavorite = false
            )
        )
    }

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
                    icon = { Icon(Icons.Outlined.Person, contentDescription = "Profile") },
                    selected = false,
                    onClick = { /* Handle navigation */ },
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
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        categories.forEachIndexed { index, (name, iconRes) ->
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.clickable { selectedCategoryIndex = index }
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
                                    textAlign = TextAlign.Center
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
                    text = "532 founds",
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

            // Service providers list
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(serviceProviders) { provider ->
                    ServiceProviderCard(
                        serviceProvider = provider,
                        onFavoriteClick = {
                            val index = serviceProviders.indexOfFirst { it.id == provider.id }
                            if (index != -1) {
                                serviceProviders[index] = serviceProviders[index].copy(
                                    isFavorite = !serviceProviders[index].isFavorite
                                )
                            }
                        }
                    )
                }

                // Add some bottom padding
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
fun ServiceProviderCard(
    serviceProvider: ServiceProvider,
    onFavoriteClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Profile image
            Image(
                painter = painterResource(id = serviceProvider.imageRes),
                contentDescription = "Profile picture of ${serviceProvider.name}",
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            // Provider details
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = serviceProvider.name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )

                    IconButton(
                        onClick = onFavoriteClick,
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = if (serviceProvider.isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = if (serviceProvider.isFavorite) Color.Red else Color.Gray
                        )
                    }
                }

                Text(
                    text = serviceProvider.profession,
                    fontSize = 14.sp,
                    color = Color.DarkGray
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Location",
                        tint = Color.Gray,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = serviceProvider.location,
                        fontSize = 12.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(start = 2.dp)
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = "Rating",
                        tint = Color(0xFFFFC107),
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "${serviceProvider.rating}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                    Text(
                        text = "${serviceProvider.reviews} Reviews",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    TarabahoTheme {
        HomeScreen()
    }
}