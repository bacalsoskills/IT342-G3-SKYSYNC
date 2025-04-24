package com.frontend.mobile.ui.activitypage

import android.app.TimePickerDialog
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
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
    val coroutineScope = rememberCoroutineScope()

    // Fetch schedule and activity when the screen is loaded
    LaunchedEffect(activityId) {
        if (token == null) {
            Toast.makeText(context, "Authorization token not found", Toast.LENGTH_SHORT).show()
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
        apiService.getUserActivities(userId = 33, token = "Bearer $token").enqueue(object : Callback<List<ActivityDTO>> {
            override fun onResponse(call: Call<List<ActivityDTO>>, response: Response<List<ActivityDTO>>) {
                if (response.isSuccessful) {
                    val activities = response.body()
                    activity = activities?.find { it.activityId == activityId }
                } else {
                    Toast.makeText(context, "Failed to fetch activity details", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<ActivityDTO>>, t: Throwable) {
                Toast.makeText(context, "Failed to fetch activity details: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })

        isLoading = false
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Activity Details") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        content = { padding ->
            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                Column(modifier = Modifier.padding(16.dp)) {
                    if (activity != null) {
                        // Display activity details
                        Text(text = "Activity: ${activity!!.name}", style = MaterialTheme.typography.h6)
                        Text(text = "Description: ${activity!!.description}", style = MaterialTheme.typography.body1)
                        Spacer(modifier = Modifier.height(16.dp))

                        if (schedule == null) {
                            // No schedule found
                            Text(
                                text = "There is no schedule for this activity.",
                                style = MaterialTheme.typography.body1,
                                color = MaterialTheme.colors.error
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = {
                                    navController.navigate(
                                        "schedule_activity/${activity!!.activityId}/${activity!!.name}/${activity!!.description}"
                                    )
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Add a Schedule")
                            }
                        } else {
                            if (!isEditing) {
                                // Display schedule details
                                Text(text = "Start Time: ${schedule!!.startTime}")
                                Text(text = "End Time: ${schedule!!.endTime}")
                                Spacer(modifier = Modifier.height(16.dp))
                                Button(onClick = { isEditing = true }) {
                                    Text("Edit Schedule")
                                }
                            } else {
                                // Edit schedule form with time pickers
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
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(if (startTime.isEmpty()) "Select Start Time" else "Start Time: $startTime")
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
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(if (endTime.isEmpty()) "Select End Time" else "End Time: $endTime")
                                }

                                Spacer(modifier = Modifier.height(16.dp))
                                Row {
                                    Button(onClick = {
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
                                            } catch (e: Exception) {
                                                Toast.makeText(context, "Failed to update schedule: ${e.message}", Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                    }) {
                                        Text("Save")
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Button(onClick = { isEditing = false }) {
                                        Text("Cancel")
                                    }
                                }
                            }
                        }
                    } else {
                        // Handle null activity gracefully
                        Text(
                            text = "Activity details are unavailable.",
                            style = MaterialTheme.typography.body1,
                            color = MaterialTheme.colors.error
                        )
                    }
                }
            }
        }
    )
}