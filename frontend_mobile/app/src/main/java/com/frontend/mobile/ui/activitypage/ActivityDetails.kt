package com.frontend.mobile.ui.activitypage

import android.app.TimePickerDialog
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.navigation.NavController
import com.frontend.mobile.R
import com.frontend.mobile.api.ApiClient
import com.frontend.mobile.api.ApiService
import com.frontend.mobile.model.ActivityDTO
import com.frontend.mobile.model.ScheduleRequestDTO
import com.frontend.mobile.model.ScheduleResponseDTO
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

@Composable
fun ActivityDetails(navController: NavController, activityId: Long) {
    val apiService = ApiClient.getClient().create(ApiService::class.java)
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    val token = sharedPreferences.getString("authToken", null)
    var schedule by remember { mutableStateOf<ScheduleResponseDTO?>(null) }
    var activity by remember { mutableStateOf<ActivityDTO?>(null) }
    var isEditing by remember { mutableStateOf(false) }
    var startTime by remember { mutableStateOf("") }
    var endTime by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()

    // Fetch schedule and activity when the screen is loaded
    LaunchedEffect(activityId) {
        if (token == null) {
            errorMessage = "Authorization token not found"
            isLoading = false
            return@LaunchedEffect
        }

        val userId = sharedPreferences.getLong("userId", -1L) // Retrieve the logged-in user's ID
        if (userId == -1L) {
            errorMessage = "User ID not found. Please log in again."
            isLoading = false
            return@LaunchedEffect
        }

        try {
            // Fetch schedule
            val scheduleResponse = apiService.getScheduleByActivityId(activityId, "Bearer $token")
            schedule = scheduleResponse
            startTime = scheduleResponse.startTime.substring(11, 16) // Extract time (HH:mm)
            endTime = scheduleResponse.endTime.substring(11, 16) // Extract time (HH:mm)
        } catch (e: Exception) {
            // If no schedule is found, set schedule to null
            schedule = null
        }

        // Fetch activity details
        apiService.getUserActivities(userId = userId, token = "Bearer $token").enqueue(object : Callback<List<ActivityDTO>> {
            override fun onResponse(call: Call<List<ActivityDTO>>, response: Response<List<ActivityDTO>>) {
                if (response.isSuccessful) {
                    val activities = response.body()
                    activity = activities?.find { it.activityId == activityId } // Match activityId
                    if (activity == null) {
                        errorMessage = "Activity not found"
                    }
                } else {
                    errorMessage = "Failed to fetch activity details: ${response.errorBody()?.string() ?: "Unknown error"}"
                }
                isLoading = false
            }

            override fun onFailure(call: Call<List<ActivityDTO>>, t: Throwable) {
                errorMessage = "Failed to fetch activity details: ${t.message}"
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
            onClick = { navController.popBackStack() },
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

        // Activity Details Section
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 200.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)
                .align(Alignment.TopCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Activity Details",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            if (isLoading) {
                CircularProgressIndicator()
            } else if (errorMessage != null) {
                Text(errorMessage!!, color = Color.Red, fontSize = 16.sp)
            } else if (activity == null) {
                Text("Activity details are unavailable.", fontSize = 16.sp)
            } else {
                androidx.compose.material3.Card( // Use Material3 Card
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp), // Use Material3 elevation
                    shape = MaterialTheme.shapes.medium
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = activity!!.name,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = activity!!.description,
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        // Schedule Information or Button
                        if (schedule == null) {
                            Text(
                                text = "No schedule set for this activity.",
                                style = MaterialTheme.typography.bodyMedium, // Use Material3 typography
                                color = MaterialTheme.colorScheme.error, // Use Material3 colorScheme
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            Button(
                                onClick = {
                                    navController.navigate(
                                        "schedule_activity/${activity!!.activityId}/${activity!!.name}/${activity!!.description}"
                                    )
                                },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2D9CDB)) // Set button color
                            ) {
                                Text("Add Schedule", color = Color.White) // Ensure text color is visible
                            }
                        } else {
                            Text(text = "Schedule:", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "Start Time: ${schedule!!.startTime.substring(11, 16)}")
                            Text(text = "End Time: ${schedule!!.endTime.substring(11, 16)}")
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                onClick = { isEditing = true },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2D9CDB)) // Set button color
                            ) {
                                Text("Edit Schedule", color = Color.White) // Ensure text color is visible
                            }
                        }

                        // Edit Schedule Section (Conditionally Visible)
                        if (isEditing) {
                            Spacer(modifier = Modifier.height(16.dp))
                            Text("Edit Schedule", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            val calendar = Calendar.getInstance()

                            Button(
                                onClick = {
                                    TimePickerDialog(
                                        context,
                                        { _, hourOfDay, minute ->
                                            startTime = String.format("%02d:%02d", hourOfDay, minute)
                                        },
                                        calendar.get(Calendar.HOUR_OF_DAY),
                                        calendar.get(Calendar.MINUTE),
                                        true
                                    ).show()
                                },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2D9CDB)) // Set button color
                            ) {
                                Text(if (startTime.isEmpty()) "Select Start Time" else "Start Time: $startTime", color = Color.White) // Ensure text color is visible
                            }

                            Button(
                                onClick = {
                                    TimePickerDialog(
                                        context,
                                        { _, hourOfDay, minute ->
                                            endTime = String.format("%02d:%02d", hourOfDay, minute)
                                        },
                                        calendar.get(Calendar.HOUR_OF_DAY),
                                        calendar.get(Calendar.MINUTE),
                                        true
                                    ).show()
                                },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2D9CDB)) // Set button color
                            ) {
                                Text(if (endTime.isEmpty()) "Select End Time" else "End Time: $endTime", color = Color.White) // Ensure text color is visible
                            }

                            Spacer(modifier = Modifier.height(16.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                Button(
                                    onClick = {
                                        coroutineScope.launch {
                                            try {
                                                apiService.editSchedule(
                                                    scheduleId = schedule!!.scheduleId,
                                                    token = "Bearer $token",
                                                    scheduleRequest = ScheduleRequestDTO(
                                                        activityId = activityId,
                                                        startTime = "${schedule!!.date}T$startTime:00",
                                                        endTime = "${schedule!!.date}T$endTime:00"
                                                    )
                                                )
                                                Toast.makeText(context, "Schedule updated successfully", Toast.LENGTH_SHORT).show()
                                                isEditing = false
                                                // Re-fetch schedule to update the displayed time
                                                try {
                                                    val updatedScheduleResponse = apiService.getScheduleByActivityId(activityId, "Bearer $token")
                                                    schedule = updatedScheduleResponse
                                                    startTime = updatedScheduleResponse.startTime.substring(11, 16)
                                                    endTime = updatedScheduleResponse.endTime.substring(11, 16)
                                                } catch (e: Exception) {
                                                    // Handle potential error during re-fetch
                                                }
                                            } catch (e: Exception) {
                                                Toast.makeText(context, "Failed to update schedule: ${e.message}", Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2D9CDB)) // Set button color
                                ) {
                                    Text("Save", color = Color.White) // Ensure text color is visible
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Button(
                                    onClick = { isEditing = false },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2D9CDB)) // Set button color
                                ) {
                                    Text("Cancel", color = Color.White) // Ensure text color is visible
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}