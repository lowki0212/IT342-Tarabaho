package com.example.mobile_tarabahoapp.AuthRepository

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile_tarabahoapp.api.RetrofitClient
import com.example.mobile_tarabahoapp.model.AuthResponse
import com.example.mobile_tarabahoapp.model.LoginRequest
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    val loginResult = MutableLiveData<AuthResponse>()
    val loginError = MutableLiveData<String>()

    fun login(username: String, password: String) {
        viewModelScope.launch {
            try {
                // Create a login request using your data class
                val loginRequest = LoginRequest(username, password)
                val response = RetrofitClient.apiService.login(loginRequest)

                if (response.isSuccessful) {
                    // If we get a successful JSON response, handle it
                    response.body()?.let { authResponse ->
                        loginResult.value = authResponse
                    } ?: run {
                        loginError.value = "Empty response from server."
                    }
                } else {
                    // For error responses, try to read the error body as plain text
                    val errorString = response.errorBody()?.string()
                    // Provide a more descriptive message based on the HTTP status code if desired
                    val errorMessage = when (response.code()) {
                        404 -> "User not found. Please register before logging in."
                        401 -> "Invalid username or password."
                        else -> "Login failed: $errorString"
                    }
                    loginError.value = errorMessage
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
                loginError.value = "An error occurred: ${ex.localizedMessage}"
            }
        }
    }
}
