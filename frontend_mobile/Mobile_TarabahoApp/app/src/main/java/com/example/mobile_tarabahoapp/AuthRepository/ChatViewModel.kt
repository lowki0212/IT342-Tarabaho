import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile_tarabahoapp.model.MessageDTO
import com.example.mobile_tarabahoapp.websocket.WebsocketManager
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import android.util.Log

class ChatViewModel(
    private val bookingId: Long,
    private val token: String
) : ViewModel() {

    private val gson = Gson()
    private val TAG = "ChatViewModel"

    private val _messages = MutableStateFlow<List<MessageDTO>>(emptyList())
    val messages: StateFlow<List<MessageDTO>> = _messages.asStateFlow()

    private val _connectionState = MutableStateFlow(ConnectionState.DISCONNECTED)
    val connectionState: StateFlow<ConnectionState> = _connectionState.asStateFlow()

    private val websocketManager = WebsocketManager(bookingId, token)

    init {
        viewModelScope.launch(Dispatchers.IO) {
            websocketManager.messagesFlow.collectLatest { jsonPayload ->
                try {
                    val message = gson.fromJson(jsonPayload, MessageDTO::class.java)
                    _messages.value = _messages.value + message
                } catch (e: JsonSyntaxException) {
                    Log.e(TAG, "Error parsing message JSON: ${e.message}")
                }
            }
        }
    }

    fun connect() {
        _connectionState.value = ConnectionState.CONNECTING
        viewModelScope.launch(Dispatchers.IO) {
            try {
                websocketManager.connect()
                _connectionState.value = ConnectionState.CONNECTED
            } catch (e: Exception) {
                Log.e(TAG, "Connection error: ${e.message}")
                _connectionState.value = ConnectionState.ERROR
            }
        }
    }

    fun disconnect() {
        viewModelScope.launch(Dispatchers.IO) {
            websocketManager.disconnect()
            _connectionState.value = ConnectionState.DISCONNECTED
        }
    }

    fun sendMessage(content: String) {
        if (content.isBlank()) return

        viewModelScope.launch(Dispatchers.IO) {
            websocketManager.sendMessage(content)
        }
    }

    // Add a way to retry connection
    fun retryConnection() {
        disconnect()
        connect()
    }

    // Handle cleanup when ViewModel is cleared
    override fun onCleared() {
        super.onCleared()
        disconnect()
    }

    enum class ConnectionState {
        DISCONNECTED,
        CONNECTING,
        CONNECTED,
        ERROR
    }
}