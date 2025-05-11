package com.example.mobile_tarabahoapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.mobile_tarabahoapp.model.RegisterRequest
import com.example.mobile_tarabahoapp.model.User
import com.example.mobile_tarabahoapp.AuthRepository.SignUpViewModel
import com.example.mobile_tarabahoapp.ui.theme.TarabahoTheme
import androidx.navigation.compose.rememberNavController

class SignUpActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TarabahoTheme {
                val navController = rememberNavController() // ✅ create this
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SignUpScreen(
                        navController = navController, // ✅ pass it here
                        onSignUpSuccess = {
                            navController.navigate("login") {
                                popUpTo(0) { inclusive = true } // clear backstack
                            }
                        }
                    )
                }
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    navController: NavController, // ✅ required now
    onSignUpSuccess: () -> Unit,
    viewModel: SignUpViewModel = viewModel()
) {
    // Form state
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }  // Backend format: YYYY-MM-DD
    var displayDate by remember { mutableStateOf("") } // Display format: DD/MM/YYYY
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var username by remember { mutableStateOf("") }
    var usernameError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    // Observe ViewModel LiveData
    val newUser by viewModel.newUser.observeAsState()
    val errorMsg by viewModel.signUpError.observeAsState()

    // On successful registration, invoke callback
    newUser?.let {
        LaunchedEffect(it) { onSignUpSuccess() }
    }

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
    ) {
        // Blue header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .background(Color(0xFF2962FF))
        ) {
            // Back button
            IconButton(
                onClick = {
                    navController.popBackStack() // this goes back to the login screen
                },
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.TopStart)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }


            // Sign Up title and login link
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Sign Up",
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Already have an account? ",
                        color = Color.White,
                        fontSize = 14.sp
                    )
                    Text(
                        text = "Log In",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier.clickable { (context as? Activity)?.finish() }
                    )
                }
            }
        }

        // Form content
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(top = 16.dp)
        ) {
            // Name fields (First and Last name in a row)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // First name
                OutlinedTextField(
                    value = firstName,
                    onValueChange = { firstName = it },
                    label = null,
                    placeholder = { Text("First Name") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(4.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color.LightGray,
                        focusedBorderColor = Color(0xFF2962FF)
                    ),
                    singleLine = true
                )

                // Last name
                OutlinedTextField(
                    value = lastName,
                    onValueChange = { lastName = it },
                    label = null,
                    placeholder = { Text("Last Name") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(4.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color.LightGray,
                        focusedBorderColor = Color(0xFF2962FF)
                    ),
                    singleLine = true
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = username,
                onValueChange = {
                    username = it
                    usernameError = null
                },
                placeholder = { Text("Username") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(4.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.LightGray,
                    focusedBorderColor = Color(0xFF2962FF)
                ),
                singleLine = true,
                isError = usernameError != null
            )

            usernameError?.let {
                Text(text = it, color = Color.Red, fontSize = 12.sp, modifier = Modifier.padding(top = 2.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Email field
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = null,
                placeholder = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(4.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.LightGray,
                    focusedBorderColor = Color(0xFF2962FF)
                ),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // City field
            OutlinedTextField(
                value = city,
                onValueChange = { city = it },
                label = null,
                placeholder = { Text("City") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(4.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.LightGray,
                    focusedBorderColor = Color(0xFF2962FF)
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Date field
            OutlinedTextField(
                value = displayDate,
                onValueChange = {
                    displayDate = it
                    // You might want to add logic to convert between display format and backend format
                    // For simplicity, we're just updating the display value here
                },
                label = null,
                placeholder = { Text("BirthDate") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(4.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.LightGray,
                    focusedBorderColor = Color(0xFF2962FF)
                ),
                singleLine = true,
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.CalendarMonth,
                        contentDescription = "Select Date",
                        tint = Color.Gray
                    )
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Phone field with country code
            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = null,
                placeholder = { Text("Phone Number") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(4.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.LightGray,
                    focusedBorderColor = Color(0xFF2962FF)
                ),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                leadingIcon = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { /* Open country code selector */ }
                    ) {
                        Text(
                            text = "🇺🇸", // Flag emoji as placeholder
                            fontSize = 20.sp
                        )
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = "Select Country Code",
                            tint = Color.Gray,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Password field
            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    passwordError = null // clear error when user types
                },
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
                },
                isError = passwordError != null
            )

            passwordError?.let {
                Text(
                    text = it,
                    color = Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start = 4.dp, top = 2.dp)
                )
            }


            Spacer(modifier = Modifier.height(24.dp))

            // Sign Up button
            Button(
                onClick = {
                    // ✅ Validate
                    val parsedDate = try {
                        val parts = displayDate.split("/")
                        if (parts.size == 3) "${parts[2]}-${parts[1].padStart(2, '0')}-${parts[0].padStart(2, '0')}" else ""
                    } catch (e: Exception) {
                        ""
                    }

                    var valid = true
                    if (username.isBlank()) {
                        usernameError = "Username is required"
                        valid = false
                    }
                    if (password.length < 8) {
                        passwordError = "Password must be at least 8 characters"
                        valid = false
                    }

                    if (valid) {
                        viewModel.register(
                            RegisterRequest(
                                firstname = firstName,
                                lastname = lastName,
                                username = username.trim(),
                                password = password,
                                email = email.trim(),
                                phoneNumber = phone.trim(),
                                location = city.trim(),
                                birthday = parsedDate // ✅ correctly formatted
                            )
                        )
                    }
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
                    text = "Sign Up",
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
fun SignUpScreenPreview() {
    val navController = rememberNavController() // ✅ Add this
    TarabahoTheme {
        SignUpScreen(navController = navController, onSignUpSuccess = {})
    }
}

