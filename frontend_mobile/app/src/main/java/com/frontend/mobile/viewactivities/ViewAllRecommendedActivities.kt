package com.frontend.mobile.viewactivities

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
fun ViewAllRecommendedActivities(
    city: String,
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
    val userId = sharedPreferences.getLong("userId", -1L)

    LaunchedEffect(city) {
        if (token != null) {
            isLoading = true
            apiService.getActivityRecommendationsByCity(city, "Bearer $token")
                .enqueue(object : Callback<List<ActivityDTO>> {
                    override fun onResponse(
                        call: Call<List<ActivityDTO>>,
                        response: Response<List<ActivityDTO>>
                    ) {
                        if (response.isSuccessful) {
                            activityRecommendations = response.body() ?: emptyList()
                        } else {
                            errorMessage = "Error: ${response.errorBody()?.string() ?: "Unknown error"}"
                        }
                        isLoading = false
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

    fun saveActivity(activity: ActivityDTO) {
        if (userId == -1L || token == null) {
            Toast.makeText(context, "Missing credentials", Toast.LENGTH_SHORT).show()
            return
        }

        apiService.saveActivity(userId, "Bearer $token", activity)
            .enqueue(object : Callback<ActivityDTO> {
                override fun onResponse(call: Call<ActivityDTO>, response: Response<ActivityDTO>) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            Toast.makeText(context, "Activity added successfully", Toast.LENGTH_SHORT).show()
                            navController.navigate("schedule_activity/${it.activityId}/${it.name}/${it.description}")
                        }
                    } else {
                        Toast.makeText(context, "Failed to add activity", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ActivityDTO>, t: Throwable) {
                    Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFA7F0F9))
    ) {
        // Back Arrow
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
                .padding(top = 80.dp),  // Adjusted padding
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.imagelogo),
                contentDescription = "SkySync Logo",
                modifier = Modifier.height(100.dp)
            )
        }

        // Main Content Card
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 200.dp, start = 20.dp, end = 20.dp, bottom = 16.dp)  // Adjusted padding
                .verticalScroll(scrollState)
                .align(Alignment.TopCenter),
        ) {
            Text(
                text = "Recommended Activities",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 16.dp)
            )

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
                        elevation = CardDefaults.cardElevation(6.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = activity.name,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = activity.description,
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                                Button(
                                    onClick = { saveActivity(activity) },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2D9CDB))
                                ) {
                                    Text("Add", color = Color.White)
                                }
                            }
                        }
                    }

                }
            }

           Button(
                onClick = { navController.navigate("user_add_activity") }, // Ensure the route matches the navigation graph
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2D9CDB)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Add Own Activity", color = Color.White)
            }
        }
    }
}