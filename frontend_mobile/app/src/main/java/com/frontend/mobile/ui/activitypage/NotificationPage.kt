package com.example.wanderways.ui.pages

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ClearAll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

import com.frontend.mobile.api.ApiClient
import com.frontend.mobile.api.ApiService
import com.frontend.mobile.model.NotificationDTO
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationPage(
    navController: NavController,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    val apiService = ApiClient.getClient().create(ApiService::class.java)
    var notifications by remember { mutableStateOf<List<String>>(emptyList()) }

    // Fetch notifications
    LaunchedEffect(Unit) {
        val userId = sharedPreferences.getLong("userId", -1L)
        val token = sharedPreferences.getString("authToken", null)
        if (userId != -1L && token != null) {
            apiService.getUserNotifications(userId, "Bearer $token").enqueue(object : Callback<List<NotificationDTO>> {
                override fun onResponse(
                    call: Call<List<NotificationDTO>>,
                    response: Response<List<NotificationDTO>>
                ) {
                    if (response.isSuccessful) {
                        // Sort notifications by ID in descending order and extract messages
                        notifications = response.body()?.sortedByDescending { it.id }?.map { it.message } ?: emptyList()
                    } else {
                        Toast.makeText(context, "Failed to fetch notifications", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<List<NotificationDTO>>, t: Throwable) {
                    Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Notifications") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        // TODO: Clear all logic
                    }) {
                        Icon(
                            imageVector = Icons.Filled.ClearAll,
                            contentDescription = "Clear All"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            notifications.forEach { message ->
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(4.dp)
                )
            }
        }
    }
}

@Composable
fun NotificationList(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        NotificationCard(title = "New Message", description = "You have a new message in your inbox")
        NotificationCard(title = "Upcoming Trip", description = "Your trip to Paris is coming up soon")
        NotificationCard(title = "Payment Successful", description = "Your payment for the booking was successful")
    }
}

@Composable
fun NotificationCard(title: String, description: String) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F0F0)),
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = title, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = description, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
