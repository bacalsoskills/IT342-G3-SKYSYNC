package com.frontend.mobile.viewactivities

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
fun ViewAllRecommendedActivities(
    city: String, // Pass the city as a parameter
    navController: NavHostController,
    onBackClick: () -> Unit
) {
    val apiService = ApiClient.getClient().create(ApiService::class.java)
    val scrollState = rememberScrollState()
    var activityRecommendations by remember { mutableStateOf<List<ActivityDTO>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    val token = sharedPreferences.getString("authToken", null)
    val userId = sharedPreferences.getLong("userId", -1L) // Retrieve user ID from SharedPreferences

    // Fetch all activity recommendations
    LaunchedEffect(city) {
        if (token != null) {
            isLoading = true
            apiService.getActivityRecommendationsByCity(city, "Bearer $token").enqueue(object : Callback<List<ActivityDTO>> {
                override fun onResponse(
                    call: Call<List<ActivityDTO>>,
                    response: Response<List<ActivityDTO>>
                ) {
                    if (response.isSuccessful) {
                        activityRecommendations = response.body() ?: emptyList()
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
        } else {
            errorMessage = "Authentication token not found"
            isLoading = false
        }
    }

    // Function to save an activity
    fun saveActivity(activity: ActivityDTO) {
        if (userId == -1L) {
            Toast.makeText(context, "User ID not found", Toast.LENGTH_SHORT).show()
            return
        }

        if (token == null) {
            Toast.makeText(context, "Authorization token not found", Toast.LENGTH_SHORT).show()
            return
        }

        val activityEntity = ActivityDTO(
            name = activity.name,
            description = activity.description,
            weatherCondition = activity.weatherCondition
        )

        apiService.saveActivity(userId, "Bearer $token", activityEntity).enqueue(object : Callback<ActivityDTO> {
            override fun onResponse(call: Call<ActivityDTO>, response: Response<ActivityDTO>) {
                if (response.isSuccessful) {
                    Toast.makeText(context, "Activity added successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Failed to add activity", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ActivityDTO>, t: Throwable) {
                Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Recommended Activities", fontSize = 20.sp) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                backgroundColor = MaterialTheme.colors.primary,
                contentColor = Color.White,
                elevation = 6.dp
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else if (errorMessage != null) {
                Text(
                    text = errorMessage!!,
                    color = Color.Red,
                    fontSize = 16.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            } else {
                activityRecommendations.forEach { activity ->
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        elevation = 4.dp,
                        backgroundColor = Color(0xFFE3F2FD),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                        ) {
                            // Display activity name
                            Text(
                                text = activity.name,
                                fontSize = 18.sp,
                                color = Color.Black,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            // Display activity description
                            Text(
                                text = activity.description,
                                fontSize = 14.sp,
                                color = Color.Gray
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            // Add Button
                            Button(
                                onClick = { saveActivity(activity) },
                                modifier = Modifier.align(Alignment.End),
                                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF2D9CDB))
                            ) {
                                Text("Add", color = Color.White)
                            }
                        }
                    }
                }
            }
        }
    }
}
