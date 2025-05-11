package com.example.mobile_tarabahoapp.AuthRepository

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.*
import com.example.mobile_tarabahoapp.api.RetrofitClient
import com.example.mobile_tarabahoapp.model.CategoryBookingRequest
import com.example.mobile_tarabahoapp.model.Worker
import com.example.mobile_tarabahoapp.model.WorkerUpdateRequest
import kotlinx.coroutines.launch
import retrofit2.Response

class WorkerViewModel : ViewModel() {

    private val api = RetrofitClient.apiService

    private val _workers = MutableLiveData<List<Worker>>(emptyList())
    val workers: LiveData<List<Worker>> = _workers

    private val _selectedWorker = MutableLiveData<Worker?>()
    val selectedWorker: LiveData<Worker?> = _selectedWorker

    private val _updateSuccess = MutableLiveData<Boolean>()
    val updateSuccess: LiveData<Boolean> = _updateSuccess

    private val _isLoading = mutableStateOf(false)
    val isLoading get() = _isLoading

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

    fun fetchWorkerById(workerId: String) {
        viewModelScope.launch {
            Log.d("WorkerViewModel", "Calling API for worker ID: $workerId")
            try {
                val response = api.getWorkerById(workerId)
                if (response.isSuccessful) {
                    Log.d("WorkerViewModel", "Fetched worker: ${response.body()}")
                    _selectedWorker.value = response.body()
                } else {
                    Log.e("WorkerViewModel", "API failed: ${response.code()} ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("WorkerViewModel", "Exception: ${e.message}")
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

    fun updateWorkerProfile(workerId: Long, request: WorkerUpdateRequest) {
        viewModelScope.launch {
            try {
                val response = api.updateWorkerProfile(workerId, request)
                _updateSuccess.postValue(response.isSuccessful)
            } catch (e: Exception) {
                Log.e("WorkerViewModel", "Update error: ${e.message}")
                _updateSuccess.postValue(false)
            }
        }
    }

    fun createChatBooking(
        request: CategoryBookingRequest,
        onResult: (Long) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.createCategoryBooking(request)
                if (response.isSuccessful) {
                    val booking = response.body()
                    if (booking != null) {
                        onResult(booking.id) // ✅ pass bookingId
                    }
                }
            } catch (e: Exception) {
                Log.e("WorkerViewModel", "Booking failed: ${e.message}")
            }
        }
    }

}
