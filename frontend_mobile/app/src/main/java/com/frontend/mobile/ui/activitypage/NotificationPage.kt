package com.example.wanderways.ui.pages

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

import com.frontend.mobile.api.ApiClient
import com.frontend.mobile.api.ApiService
import com.frontend.mobile.model.NotificationDTO
import com.frontend.mobile.R // Import your R file for resources
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
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

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
                        notifications = response.body()?.sortedByDescending { it.id }?.map { it.message } ?: emptyList()
                        isLoading = false
                    } else {
                        errorMessage = "Failed to fetch notifications: ${response.errorBody()?.string() ?: "Unknown error"}"
                        isLoading = false
                    }
                }

                override fun onFailure(call: Call<List<NotificationDTO>>, t: Throwable) {
                    errorMessage = "Error: ${t.message}"
                    isLoading = false
                }
            })
        } else {
            errorMessage = "User ID or token not found"
            isLoading = false
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFA7F0F9))
    ) {
        // Back Button
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .padding(top = 40.dp, start = 16.dp)
                .align(Alignment.TopStart)
        ) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
        }
        // Logo
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 80.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.imagelogo),
                contentDescription = "Your App Logo", // Update content description
                modifier = Modifier.height(100.dp)
            )
        }

        // Notifications List
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 200.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)
                .align(Alignment.TopCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Notifications",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                IconButton(onClick = {
                    // TODO: Clear all logic
                    Toast.makeText(context, "Clear All Clicked", Toast.LENGTH_SHORT).show() // Placeholder
                }) {
                    Icon(
                        imageVector = Icons.Filled.ClearAll,
                        contentDescription = "Clear All"
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            if (isLoading) {
                CircularProgressIndicator()
            } else if (errorMessage != null) {
                Text(errorMessage!!, color = Color.Red, fontSize = 16.sp)
            } else if (notifications.isEmpty()) {
                Text("No notifications found.", fontSize = 16.sp)
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(notifications) { message ->
                        NotificationCard(message = message)
                    }
                }
            }
        }
    }
}

@Composable
fun NotificationCard(message: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp), // Use CardDefaults for elevation
        shape = MaterialTheme.shapes.medium
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = message,
                fontSize = 16.sp,
                color = Color.Black
            )
        }
    }
}