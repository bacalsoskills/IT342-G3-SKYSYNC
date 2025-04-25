package com.frontend.mobile.viewactivities

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.frontend.mobile.api.ApiClient
import com.frontend.mobile.api.ApiService
import com.frontend.mobile.model.ScheduleRequestDTO
import com.frontend.mobile.model.ScheduleResponseDTO
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ScheduleActivity(
    activityId: Long,
    activityName: String,
    activityDescription: String,
    navController: NavHostController
) {
    val apiService = ApiClient.getClient().create(ApiService::class.java)
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    val token = sharedPreferences.getString("authToken", null)
    val userId = sharedPreferences.getLong("userId", -1L)

    // Automatically set the current date
    val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    var selectedStartTime by remember { mutableStateOf("") }
    var selectedEndTime by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    fun createSchedule() {
        if (userId == -1L || token == null) {
            Toast.makeText(context, "User ID or token not found", Toast.LENGTH_SHORT).show()
            return
        }

        if (selectedStartTime.isEmpty() || selectedEndTime.isEmpty()) {
            Toast.makeText(context, "Please select start and end times", Toast.LENGTH_SHORT).show()
            return
        }

        // Construct startTime and endTime without timezone offset
        val startTime = "$currentDate" + "T" + "$selectedStartTime"
        val endTime = "$currentDate" + "T" + "$selectedEndTime"

        val scheduleRequest = ScheduleRequestDTO(
            activityId = activityId,
            startTime = startTime,
            endTime = endTime
        )

        isLoading = true
        apiService.createSchedule(userId, "Bearer $token", scheduleRequest).enqueue(object : Callback<ScheduleResponseDTO> {
            override fun onResponse(call: Call<ScheduleResponseDTO>, response: Response<ScheduleResponseDTO>) {
                isLoading = false
                if (response.isSuccessful) {
                    Toast.makeText(context, "Schedule created successfully", Toast.LENGTH_SHORT).show()
                    navController.popBackStack() // Navigate back to the previous screen
                } else {
                    Toast.makeText(context, "Failed to create schedule", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ScheduleResponseDTO>, t: Throwable) {
                isLoading = false
                Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Schedule Activity") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Activity: $activityName", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text("Description: $activityDescription", fontSize = 16.sp)
                Text("Date: $currentDate", fontSize = 16.sp, fontWeight = FontWeight.Medium)

                // Start Time Picker
                Button(
                    onClick = {
                        val calendar = Calendar.getInstance()
                        TimePickerDialog(
                            context,
                            { _, hourOfDay, minute ->
                                selectedStartTime = String.format("%02d:%02d:00", hourOfDay, minute)
                            },
                            calendar.get(Calendar.HOUR_OF_DAY),
                            calendar.get(Calendar.MINUTE),
                            true
                        ).show()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(if (selectedStartTime.isEmpty()) "Select Start Time" else "Start Time: $selectedStartTime")
                }

                // End Time Picker
                Button(
                    onClick = {
                        val calendar = Calendar.getInstance()
                        TimePickerDialog(
                            context,
                            { _, hourOfDay, minute ->
                                selectedEndTime = String.format("%02d:%02d:00", hourOfDay, minute)
                            },
                            calendar.get(Calendar.HOUR_OF_DAY),
                            calendar.get(Calendar.MINUTE),
                            true
                        ).show()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(if (selectedEndTime.isEmpty()) "Select End Time" else "End Time: $selectedEndTime")
                }

                Button(
                    onClick = { createSchedule() },
                    enabled = !isLoading,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(if (isLoading) "Creating..." else "Create Schedule")
                }
            }
        }
    )
}

