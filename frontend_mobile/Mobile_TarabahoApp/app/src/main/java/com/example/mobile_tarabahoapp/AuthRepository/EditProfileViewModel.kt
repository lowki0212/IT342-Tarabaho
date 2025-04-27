package com.example.mobile_tarabahoapp.AuthRepository

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.MutableLiveData
import com.example.mobile_tarabahoapp.api.RetrofitClient
import com.example.mobile_tarabahoapp.model.ProfileUpdateRequest
import com.example.mobile_tarabahoapp.model.User
import kotlinx.coroutines.launch
import retrofit2.Response

class EditProfileViewModel : ViewModel() {

    val updateResult = MutableLiveData<User?>()
    val updateError = MutableLiveData<String>()
    val currentUser = MutableLiveData<User?>()

    fun updateProfile(request: ProfileUpdateRequest) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.updateProfile(request)
                if (response.isSuccessful) {
                    updateResult.value = response.body()
                } else {
                    updateError.value = response.errorBody()?.string() ?: "Unknown error occurred"
                }
            } catch (e: Exception) {
                updateError.value = "Exception occurred: ${e.localizedMessage}"
            }
        }
    }

    fun loadCurrentUser() {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.getCurrentUser()
                if (response.isSuccessful) {
                    currentUser.value = response.body()
                } else {
                    updateError.value = "Failed to load user data"
                }
            } catch (e: Exception) {
                updateError.value = "Exception: ${e.localizedMessage}"
            }
        }
    }
}
