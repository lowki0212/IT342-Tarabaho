package com.example.mobile_tarabahoapp.AuthRepository

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile_tarabahoapp.api.RetrofitClient
import com.example.mobile_tarabahoapp.model.AuthResponse
import com.example.mobile_tarabahoapp.model.LoginRequest
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
                        TokenManager.saveToken(authResponse.token)
                        loginResult.value = authResponse
                    } ?: run {
                        loginError.value = "Empty response from server."
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    val message = response.code().let {
                        when (it) {
                            401 -> "Invalid username or password"
                            else -> "Login failed: $errorBody"
                        }
                    }
                    loginError.value = message
                }
            } catch (e: Exception) {
                loginError.value = "An error occurred: ${e.localizedMessage}"
            }
        }
    }
}
