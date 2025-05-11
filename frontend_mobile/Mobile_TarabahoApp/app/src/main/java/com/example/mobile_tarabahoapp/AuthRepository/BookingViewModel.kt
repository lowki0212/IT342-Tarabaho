package com.example.mobile_tarabahoapp.AuthRepository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile_tarabahoapp.api.RetrofitClient
import com.example.mobile_tarabahoapp.model.Booking
import com.example.mobile_tarabahoapp.model.CategoryBookingRequest
import com.example.mobile_tarabahoapp.model.PaymentMethod
import com.example.mobile_tarabahoapp.model.RatingRequest
import kotlinx.coroutines.launch

class BookingViewModel : ViewModel() {

    private val api = RetrofitClient.apiService
    val activeBookings = MutableLiveData<List<Booking>>()
    val pastBookings = MutableLiveData<List<Booking>>()
    val error = MutableLiveData<String>()

    fun fetchWorkerBookings() {
        viewModelScope.launch {
            try {
                val response = api.getWorkerBookings()
                if (response.isSuccessful) {
                    val bookings = response.body() ?: emptyList()
                    activeBookings.value = bookings.filter {
                        it.status in listOf("PENDING", "ACCEPTED", "IN_PROGRESS", "WORKER_COMPLETED")
                    }
                    pastBookings.value = bookings.filter {
                        it.status in listOf("REJECTED", "CANCELLED", "COMPLETED")
                    }
                } else {
                    error.value = "⚠️ Failed to load bookings: ${response.code()}"
                }
            } catch (e: Exception) {
                error.value = "⚠️ ${e.localizedMessage ?: "Network error"}"
            }
        }
    }

    fun acceptBooking(bookingId: Long) {
        viewModelScope.launch {
            try {
                val response = api.acceptBooking(bookingId)
                if (response.isSuccessful) {
                    updateBookingStatus(bookingId, "ACCEPTED")
                    getBookingById(bookingId)
                }
            } catch (e: Exception) {
                Log.e("BookingViewModel", "Accept error: ${e.message}")
            }
        }
    }

    fun rejectBooking(bookingId: Long) {
        viewModelScope.launch {
            try {
                val response = api.rejectBooking(bookingId)
                if (response.isSuccessful) {
                    updateBookingStatus(bookingId, "REJECTED")
                }
            } catch (e: Exception) {
                Log.e("BookingViewModel", "Reject error: ${e.message}")
            }
        }
    }

    fun completeBooking(bookingId: Long) {
        viewModelScope.launch {
            try {
                val response = api.completeBooking(bookingId)
                if (response.isSuccessful) {
                    updateBookingStatus(bookingId, "WORKER_COMPLETED")
                    getBookingById(bookingId)
                }
            } catch (e: Exception) {
                Log.e("BookingViewModel", "Complete error: ${e.message}")
            }
        }
    }

    private fun updateBookingStatus(bookingId: Long, newStatus: String) {
        val updated = activeBookings.value?.map {
            if (it.id == bookingId) it.copy(status = newStatus) else it
        } ?: emptyList()

        activeBookings.value = updated.filter {
            it.status in listOf("PENDING", "ACCEPTED", "IN_PROGRESS", "WORKER_COMPLETED")
        }
        pastBookings.value = updated.filter {
            it.status in listOf("REJECTED", "CANCELLED", "COMPLETED")
        }
    }

    // ✅ Fixed version → paymentMethod is now PaymentMethod enum type
        fun createBooking(
            workerId: Long,
            categoryName: String,
            paymentMethod: PaymentMethod, // <-- ENUM, not String anymore
            jobDetails: String,
            onSuccess: (Long) -> Unit,
            onError: (String) -> Unit
        ) {
            viewModelScope.launch {
                try {
                    // ✅ Automatically sends "CASH", "GCASH", etc. → backend will accept
                    val request = CategoryBookingRequest(workerId, categoryName, paymentMethod, jobDetails)
                    val response = api.createCategoryBooking(request)

                    if (response.isSuccessful) {
                        val booking = response.body()
                        onSuccess(booking?.id ?: 0)
                    } else {
                        onError("Booking failed")
                    }
                } catch (e: Exception) {
                    onError(e.message ?: "Unknown error")
                }
            }
        }

    fun checkBookingStatus(bookingId: Long, onStatusFetched: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val response = api.getBookingStatus(bookingId)
                if (response.isSuccessful) {
                    response.body()?.let { statusResponse ->
                        onStatusFetched(statusResponse.status)
                    }
                }
            } catch (e: Exception) {
                // Handle errors
            }
        }
    }

    val selectedBooking = MutableLiveData<Booking?>()

    fun getBookingById(bookingId: Long) {
        viewModelScope.launch {
            try {
                val response = api.getBookingById(bookingId)
                if (response.isSuccessful) {
                    selectedBooking.value = response.body()
                } else {
                    selectedBooking.value = null
                }
            } catch (e: Exception) {
                selectedBooking.value = null
            }
        }
    }

    fun startBooking(bookingId: Long, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val response = api.startBooking(bookingId)
                if (response.isSuccessful) {
                    getBookingById(bookingId) // refresh booking to update UI
                    onSuccess()
                } else {
                    onError("Failed to start booking")
                }
            } catch (e: Exception) {
                onError(e.message ?: "Unknown error")
            }
        }
    }

    fun acceptCompletion(bookingId: Long, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val response = api.acceptBookingCompletion(bookingId)
                if (response.isSuccessful) {
                    onSuccess()
                } else {
                    onError("Failed to complete the booking.")
                }
            } catch (e: Exception) {
                onError(e.message ?: "Unknown error")
            }
        }
    }

    fun submitRating(bookingId: Long, rating: Int, comment: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val request = RatingRequest(bookingId, rating, comment)
                val response = api.submitRating(request)
                if (response.isSuccessful) {
                    onSuccess()
                } else {
                    onError("Failed to submit rating.")
                }
            } catch (e: Exception) {
                onError(e.message ?: "Unknown error")
            }
        }
    }
    fun cancelBooking(bookingId: Long, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.cancelBooking(bookingId)
                if (response.isSuccessful) {
                    onResult(true, "Booking cancelled successfully.")
                    fetchWorkerBookings() // Optional reload
                } else {
                    onResult(false, "Failed to cancel booking: ${response.code()}")
                }
            } catch (e: Exception) {
                onResult(false, "Error: ${e.localizedMessage}")
            }
        }
    }



    private val _userBookings = MutableLiveData<List<Booking>>()
    val userBookings: LiveData<List<Booking>> = _userBookings

    private val _userBookingError = MutableLiveData<String?>()
    val userBookingError: LiveData<String?> = _userBookingError

    fun fetchUserBookings() {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.getUserBookings()
                if (response.isSuccessful) {
                    _userBookings.postValue(response.body() ?: emptyList())
                } else {
                    val msg = response.errorBody()?.string() ?: "Failed to fetch bookings"
                    _userBookingError.postValue(msg)
                }
            } catch (e: Exception) {
                _userBookingError.postValue("Error: ${e.localizedMessage}")
            }
        }
    }



}


