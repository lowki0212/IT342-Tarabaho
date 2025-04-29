package com.example.mobile_tarabahoapp.AuthRepository

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile_tarabahoapp.api.ApiService
import com.example.mobile_tarabahoapp.api.RetrofitClient
import com.example.mobile_tarabahoapp.model.Worker
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Response


class WorkerViewModel : ViewModel() {

    private val _workers = MutableLiveData<List<Worker>>(emptyList())
    val workers: LiveData<List<Worker>> = _workers

    private val _isLoading = mutableStateOf(false)
    val isLoading get() = _isLoading

    private val api = RetrofitClient.apiService

    fun fetchWorkers() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response: Response<List<Worker>> = api.getAllWorkers()
                if (response.isSuccessful && response.body() != null) {
                    _workers.value = response.body()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun fetchWorkersByCategory(categoryName: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = api.getWorkersByCategory(categoryName)
                if (response.isSuccessful && response.body() != null) {
                    _workers.value = response.body()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

}