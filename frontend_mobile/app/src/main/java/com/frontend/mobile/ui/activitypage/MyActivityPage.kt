package com.frontend.mobile.ui.activitypage

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.frontend.mobile.api.ApiClient
import com.frontend.mobile.api.ApiService
import com.frontend.mobile.model.ActivityDTO
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun MyActivityPage(navController: NavHostController, onBackClick: () -> Unit) {
    val apiService = ApiClient.getClient().create(ApiService::class.java)
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    val token = sharedPreferences.getString("authToken", null)
    val userId = sharedPreferences.getLong("userId", -1L) // Retrieve user ID from SharedPreferences

    var activities by remember { mutableStateOf<List<ActivityDTO>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Fetch user activities
    LaunchedEffect(Unit) {
        if (userId == -1L || token == null) {
            errorMessage = "User ID or token not found"
            isLoading = false
            return@LaunchedEffect
        }

        apiService.getUserActivities(userId, "Bearer $token").enqueue(object : Callback<List<ActivityDTO>> {
            override fun onResponse(call: Call<List<ActivityDTO>>, response: Response<List<ActivityDTO>>) {
                if (response.isSuccessful) {
                    activities = response.body()?.sortedByDescending { it.activityId } ?: emptyList() // Sort by activityId in descending order
                    isLoading = false
                } else {
                    errorMessage = "Error: ${response.errorBody()?.string() ?: "Unknown error"}"
                    isLoading = false
                }
            }

            override fun onFailure(call: Call<List<ActivityDTO>>, t: Throwable) {
                errorMessage = "Error: ${t.message}"
                isLoading = false
            }
        })
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Activities") },
                navigationIcon = {
                    IconButton(onClick = { onBackClick() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                backgroundColor = Color(0xFF1976D2),
                contentColor = Color.White
            )
        },
        content = { padding ->
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (errorMessage != null) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(errorMessage!!, color = Color.Red, fontSize = 16.sp)
                }
            } else if (activities.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No activities found.", fontSize = 16.sp)
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    items(activities) { activity ->
                        ActivityCard(activity = activity, navController = navController)
                    }
                }
            }
        }
    )
}

@Composable
fun ActivityCard(activity: ActivityDTO, navController: NavHostController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = 6.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = activity.name,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = activity.description,
                fontSize = 14.sp,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    navController.navigate("activity_details/${activity.activityId}")
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("View Details")
            }
        }
    }
}
