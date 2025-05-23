package com.example.mobile_tarabahoapp

import ChatViewModel
import MessagesScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mobile_tarabahoapp.AuthRepository.LoginViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.livedata.observeAsState
import com.example.mobile_tarabahoapp.AuthRepository.BookingViewModel
import com.example.mobile_tarabahoapp.AuthRepository.ChatViewModelFactory
import com.example.mobile_tarabahoapp.ui.theme.TarabahoTheme
import com.example.mobile_tarabahoapp.utils.TokenManager


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TarabahoTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "login") {

                        composable("login") {
                            LoginScreen(navController)
                        }
                        composable("signup") {
                            SignUpScreen(
                                navController = navController,
                                onSignUpSuccess = {
                                    navController.popBackStack()
                                    navController.navigate("login")
                                }
                            )
                        }
                        composable("home") {
                            HomeScreen(navController)
                        }
                        composable("settings") {
                            SettingsScreen(navController)
                        }
                        composable("edit_profile") {
                            EditProfileScreen(navController)
                        }
                        composable("worker_signin") {
                            WorkerSignInScreen(navController)
                        }
                        composable("worker_home") {
                            WorkerHomeScreen(navController)
                        }
                        composable(route = "profilesettings") {
                            EditProfileScreen(navController = navController)
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

                        composable("booking_status_rejected") {
                            BookingStatusScreen(navController, initialStatus = BookingStatusState.REJECTED)
                        }
                        composable("booking_details/{bookingId}") { backStackEntry ->
                            val bookingId = backStackEntry.arguments?.getString("bookingId") ?: ""
                            BookingDetailsScreen(navController, bookingId = bookingId)
                        }
                        // ✅ FIXED: Added missing route for navigating to a specific worker's detail screen
                        composable("worker_details/{workerId}") { backStackEntry ->
                            val workerIdString = backStackEntry.arguments?.getString("workerId") ?: return@composable
                            val workerId = workerIdString.toLongOrNull() ?: return@composable
                            WorkerDetailsScreen(navController = navController, workerId = workerId)

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

                        composable(route = "chat/{bookingId}") { backStackEntry ->
                            val bookingId = backStackEntry.arguments?.getString("bookingId")?.toLongOrNull() ?: 0L
                            val token = TokenManager.getToken() ?: ""

                            val factory = ChatViewModelFactory(bookingId, token)
                            val chatViewModel: ChatViewModel = viewModel(factory = factory)

                            // These two parameters are required for MessagesScreen according to your implementation
                            val currentUserId = TokenManager.getCurrentUserId()
                            val isWorker = TokenManager.isWorker()

                            MessagesScreen(
                                navController = navController,
                                chatViewModel = chatViewModel,
                                currentUserId = currentUserId,
                                isWorker = isWorker
                            )
                        }


                    }
                }
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController, viewModel: LoginViewModel = viewModel()) {
    // Email and password states—initialize with default values or empty strings as needed.
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var rememberMe by remember { mutableStateOf(false) }

    // Observe login result and error messages from the ViewModel.
    val loginResult by viewModel.loginResult.observeAsState()
    val loginError by viewModel.loginError.observeAsState()

    // If loginResult is not null, navigate to the home screen.
    loginResult?.let { token ->
        // You may choose to store token, then navigate.
        LaunchedEffect(token) {
            // Optionally, perform any token storage in SharedPreferences here.
            navController.navigate("home") {
                // Clear the back stack if desired.
                popUpTo("login") { inclusive = true }
            }
        }
    }

    // Optionally, display error message on the screen.
    val errorMessage = loginError ?: ""

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Blue header with logo
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(Color(0xFF2962FF)),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "T A R A B A H",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search Icon",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Tara Trabaho!",
                    color = Color.White,
                    fontSize = 12.sp
                )
            }
        }

        // Login form
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .offset(y = (-20).dp),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Google login button
                OutlinedButton(
                    onClick = { /* Handle Google login */ },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(4.dp),
                    border = BorderStroke(1.dp, Color.LightGray)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_google),
                            contentDescription = "Google Icon",
                            modifier = Modifier.size(18.dp),
                            tint = Color.Unspecified
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Continue with Google")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Or login with",
                    fontSize = 14.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Email field
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = null,
                    placeholder = { Text("Username") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(4.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color.LightGray,
                        focusedBorderColor = Color(0xFF2962FF)
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Password field
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = null,
                    placeholder = { Text("Password") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(4.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color.LightGray,
                        focusedBorderColor = Color(0xFF2962FF)
                    ),
                    singleLine = true,
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = if (passwordVisible) "Hide password" else "Show password",
                                tint = Color.Gray
                            )
                        }
                    }
                )

                // Display error message if any
                if (errorMessage.isNotEmpty()) {
                    Text(
                        text = errorMessage,
                        color = Color.Red,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Remember me and Forgot password
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = rememberMe,
                            onCheckedChange = { rememberMe = it },
                            colors = CheckboxDefaults.colors(
                                checkedColor = Color(0xFF2962FF)
                            )
                        )
                        Text(
                            text = "Remember me",
                            fontSize = 14.sp
                        )
                    }

                    TextButton(onClick = { navController.navigate("worker_signin") }) {
                        Text(
                            text = "Switch to Worker?",
                            color = Color(0xFF2962FF),
                            fontSize = 14.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Login button
                Button(
                    onClick = {
                        // Call login in the ViewModel.
                        viewModel.login(email, password)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(4.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF2962FF)
                    )
                ) {
                    Text(
                        text = "Log In",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Sign up text
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Don't have an account?",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                TextButton(onClick = { navController.navigate("signup") }) {
                    Text(
                        text = "Sign Up",
                        color = Color(0xFF2962FF),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    TarabahoTheme {
        LoginScreen(rememberNavController())
    }
}