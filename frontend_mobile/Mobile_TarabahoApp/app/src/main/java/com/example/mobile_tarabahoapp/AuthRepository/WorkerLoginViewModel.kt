package com.example.mobile_tarabahoapp.AuthRepository

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.mobile_tarabahoapp.api.RetrofitClient
import com.example.mobile_tarabahoapp.model.AuthResponse
import com.example.mobile_tarabahoapp.model.LoginRequest
import com.example.mobile_tarabahoapp.model.Worker
import com.example.mobile_tarabahoapp.utils.TokenManager
import kotlinx.coroutines.launch

class WorkerLoginViewModel : ViewModel() {

    val loginResult = MutableLiveData<AuthResponse>()
    val loginError = MutableLiveData<String>()

    fun login(username: String, password: String) {
        viewModelScope.launch {
            try {
                val loginRequest = LoginRequest(username, password)
                val response = RetrofitClient.apiService.loginWorker(loginRequest)

                if (response.isSuccessful) {
                    response.body()?.let { authResponse ->
                        // ✅ Save JWT token
                        TokenManager.saveToken(authResponse.token)

                        // ✅ Save workerId directly from AuthResponse
                        if (authResponse.workerId != null) {
                            TokenManager.saveWorkerId(authResponse.workerId)
                            loginResult.value = authResponse // ✅ Trigger success
                        } else {
                            loginError.value = "⚠️ Login succeeded but worker ID is missing."
                        }

                    } ?: run {
                        loginError.value = "⚠️ Empty response from server."
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    val message = when (response.code()) {
                        401 -> "Invalid username or password"
                        else -> "Login failed: $errorBody"
                    }
                    loginError.value = message
                }
            } catch (e: Exception) {
                loginError.value = "⚠️ An error occurred: ${e.localizedMessage}"
            }
        }
    }

    fun logout(navController: NavController) {
        // Clear the saved token and worker ID
        TokenManager.clearToken()
        TokenManager.saveWorkerId(-1)

        // Navigate to worker_signin and clear backstack so user can't press back
        navController.navigate("worker_signin") {
            popUpTo(0) { inclusive = true }
        }
    }

}
