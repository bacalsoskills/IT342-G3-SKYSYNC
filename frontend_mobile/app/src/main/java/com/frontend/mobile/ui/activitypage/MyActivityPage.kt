package com.frontend.mobile.ui.activitypage

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.navigation.NavHostController
import com.frontend.mobile.R
import com.frontend.mobile.api.ApiClient
import com.frontend.mobile.api.ApiService
import com.frontend.mobile.model.ActivityDTO
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun MyActivityPage(navController: NavHostController, onBackClick: () -> Unit) {
    val apiService = remember { ApiClient.getClient().create(ApiService::class.java) }
    val context = LocalContext.current
    val sharedPreferences = remember { context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE) }
    val token = remember { sharedPreferences.getString("authToken", null) }
    val userId = remember { sharedPreferences.getLong("userId", -1L) }

    var activities by remember { mutableStateOf<List<ActivityDTO>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        if (userId == -1L || token == null) {
            errorMessage = "User ID or token not found"
            isLoading = false
            return@LaunchedEffect
        }

        apiService.getUserActivities(userId, "Bearer $token").enqueue(object : Callback<List<ActivityDTO>> {
            override fun onResponse(call: Call<List<ActivityDTO>>, response: Response<List<ActivityDTO>>) {
                if (response.isSuccessful) {
                    activities = response.body()?.sortedByDescending { it.activityId } ?: emptyList()
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
                contentDescription = "SkySync Logo",
                modifier = Modifier.height(100.dp)
            )
        }

        // Activities List
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 200.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)
                .align(Alignment.TopCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "My Activities",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            if (isLoading) {
                CircularProgressIndicator()
            } else if (errorMessage != null) {
                Text(errorMessage!!, color = Color.Red, fontSize = 16.sp)
            } else if (activities.isEmpty()) {
                Text("No activities found.", fontSize = 16.sp)
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(activities) { activity ->
                        ActivityCard(activity = activity, navController = navController)
                    }
                }
            }
        }
    }
}

@Composable
fun ActivityCard(activity: ActivityDTO, navController: NavHostController) {
    androidx.compose.material3.Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Row( // Use Row to align items horizontally
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween // Push items to ends
        ) {
            Column(
                modifier = Modifier.weight(1f) // Allow text to take up available space
            ) {
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
            }
            Button(
                onClick = {
                    navController.navigate("activity_details/${activity.activityId}")
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2D9CDB)) // Use the specified color
            ) {
                Text("View Details")
            }
        }
    }
}