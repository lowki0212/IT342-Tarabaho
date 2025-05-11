package com.example.mobile_tarabahoapp

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.mobile_tarabahoapp.websocket.WebsocketManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    bookingId: Long,
    token: String,
    navController: NavController
) {
    val messages = remember { mutableStateListOf<String>() }
    var newMessage by remember { mutableStateOf("") }

    val websocketManager = remember {
        WebsocketManager(
            bookingId = bookingId,
            token = token
        ) { receivedMessage ->
            messages.add(receivedMessage)
        }
    }

    DisposableEffect(Unit) {
        websocketManager.connect()

        onDispose {
            websocketManager.disconnect()
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Chat for Booking #$bookingId") }
        )

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(8.dp)
        ) {
            items(messages) { message ->
                Text(text = message)
            }
        }

        Row(modifier = Modifier.padding(8.dp)) {
            TextField(
                value = newMessage,
                onValueChange = { newMessage = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Type a message...") }
            )
            IconButton(onClick = {
                if (newMessage.isNotBlank()) {
                    websocketManager.sendMessage(newMessage)
                    newMessage = ""
                }
            }) {
                Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send")
            }
        }
    }
}
