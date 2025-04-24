package com.frontend.mobile.ui.activitypage

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.frontend.mobile.viewactivities.ViewAllRecommendedActivities
import com.frontend.mobile.R
import com.frontend.mobile.api.ApiClient
import com.frontend.mobile.api.ApiService
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.frontend.mobile.model.DailyWeatherDTO
import com.frontend.mobile.model.WardrobeRecommendation
import com.frontend.mobile.model.ActivityDTO
import com.frontend.mobile.model.NotificationDTO


@Composable
fun HomePage(navController: NavHostController) {
    val gradientBackground = Brush.verticalGradient(
        colors = listOf(Color(0xFFB2FEFA), Color(0xFF0ED2F7))
    )

    val scrollState = rememberScrollState()
    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    val apiService = ApiClient.getClient().create(ApiService::class.java)

    // State to hold weather data
    var weatherData by remember { mutableStateOf<DailyWeatherDTO?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // State for city input
    var cityInput by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }

    // Load the last saved city from SharedPreferences
    LaunchedEffect(Unit) {
        city = sharedPreferences.getString("lastCity", "Cebu") ?: "Cebu"
        cityInput = city
    }

    // Save the city to SharedPreferences when it changes
    fun saveCityToPreferences(newCity: String) {
        sharedPreferences.edit().putString("lastCity", newCity).apply()
    }

    // State to hold wardrobe data
    var wardrobeData by remember { mutableStateOf<WardrobeRecommendation?>(null) }

    // State to hold activity data
    var activityData by remember { mutableStateOf<List<ActivityDTO>>(emptyList()) }

    // State to hold notifications
    var notifications by remember { mutableStateOf<List<String>>(emptyList()) }
    var isDropdownVisible by remember { mutableStateOf(false) }
    var isLoadingNotifications by remember { mutableStateOf(false) }

    // Fetch weather data
    LaunchedEffect(city) {
        if (city.isNotEmpty()) {
            val token = sharedPreferences.getString("authToken", null)
            if (token == null) {
                errorMessage = "Authentication token not found"
                isLoading = false
                return@LaunchedEffect
            }
            isLoading = true
            apiService.getTodaysWeatherByCity(city, "Bearer $token").enqueue(object : Callback<DailyWeatherDTO> {
                override fun onResponse(call: Call<DailyWeatherDTO>, response: Response<DailyWeatherDTO>) {
                    if (response.isSuccessful) {
                        Log.d("API Response", "Body: ${response.body()}")
                        weatherData = response.body()
                        isLoading = false
                    } else {
                        val errorBody = response.errorBody()?.string()
                        Log.e("API Error", "Code: ${response.code()}, Error: $errorBody")
                        errorMessage = "Error: ${errorBody ?: "Unknown error"}"
                        isLoading = false
                    }
                }

                override fun onFailure(call: Call<DailyWeatherDTO>, t: Throwable) {
                    Log.e("API Failure", "Error: ${t.message}")
                    errorMessage = "Error: ${t.message}"
                    isLoading = false
                }
            })
        }
    }

    // Fetch wardrobe data/////////////////////////////////////////////////////////////////////////////////////////////////////////
    LaunchedEffect(city) {
        if (city.isNotEmpty()) {
            val token = sharedPreferences.getString("authToken", null)
            if (token == null) {
                errorMessage = "Authentication token not found"
                isLoading = false
                return@LaunchedEffect
            }
            apiService.getTodayWardrobeByCity(city, "Bearer $token").enqueue(object : Callback<List<WardrobeRecommendation>> {
                override fun onResponse(
                    call: Call<List<WardrobeRecommendation>>,
                    response: Response<List<WardrobeRecommendation>>
                ) {
                    if (response.isSuccessful) {
                        wardrobeData = response.body()?.firstOrNull() // Get the first theme
                    } else {
                        Log.e("API Error", "Code: ${response.code()}, Error: ${response.errorBody()?.string()}")
                    }
                }

                override fun onFailure(call: Call<List<WardrobeRecommendation>>, t: Throwable) {
                    Log.e("API Failure", "Error: ${t.message}")
                }
            })
        }
    }

    // Fetch activity data
    LaunchedEffect(city) {
        if (city.isNotEmpty()) {
            val token = sharedPreferences.getString("authToken", null)
            if (token == null) {
                errorMessage = "Authentication token not found"
                return@LaunchedEffect
            }
            apiService.getActivityRecommendationsByCity(city, "Bearer $token").enqueue(object : Callback<List<ActivityDTO>> {
                override fun onResponse(
                    call: Call<List<ActivityDTO>>,
                    response: Response<List<ActivityDTO>>
                ) {
                    if (response.isSuccessful) {
                        activityData = response.body()?.take(3) ?: emptyList() // Display only the first 3 activities
                    } else {
                        Log.e("API Error", "Code: ${response.code()}, Error: ${response.errorBody()?.string()}")
                    }
                }

                override fun onFailure(call: Call<List<ActivityDTO>>, t: Throwable) {
                    Log.e("API Failure", "Error: ${t.message}")
                }
            })
        }
    }

    // Fetch notifications
    fun fetchNotifications() {
        Log.d("Notifications", "fetchNotifications called") // Debugging

        val userId = sharedPreferences.getLong("userId", -1L)
        val token = sharedPreferences.getString("authToken", null)
        if (userId != -1L && token != null) {
            isLoadingNotifications = true // Start loading
            apiService.getUserNotifications(userId, "Bearer $token").enqueue(object : Callback<List<NotificationDTO>> {
                override fun onResponse(
                    call: Call<List<NotificationDTO>>,
                    response: Response<List<NotificationDTO>>
                ) {
                    isLoadingNotifications = false // Stop loading
                    if (response.isSuccessful) {
                        // Sort notifications by ID in descending order and take the top 5
                        notifications = response.body()
                            ?.sortedByDescending { it.id }
                            ?.take(5) // Get the 5 most recent notifications
                            ?.map { it.message } ?: emptyList()

                        Log.d("Notifications", "Fetched Notifications: $notifications") // Debugging
                    } else {
                        Toast.makeText(context, "Failed to fetch notifications", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<List<NotificationDTO>>, t: Throwable) {
                    isLoadingNotifications = false // Stop loading
                    Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Log.e("Notifications", "User ID or token is missing") // Debugging
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBackground)
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        // Logo
        Image(
            painter = painterResource(id = R.drawable.imagelogo),
            contentDescription = "SkySync Logo",
            modifier = Modifier
                .height(140.dp)
                .align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Header with Dropdown Menu
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Weather",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                // Notification Dropdown
                var notificationExpanded by remember { mutableStateOf(false) }

                Box {
                    IconButton(onClick = {
                        fetchNotifications() // Fetch notifications when the dropdown is opened
                        notificationExpanded = true
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.notification), // Replace with your notification icon
                            contentDescription = "Notifications",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    DropdownMenu(
                        expanded = notificationExpanded,
                        onDismissRequest = { notificationExpanded = false }
                    ) {
                        // Display notifications
                        if (notifications.isNotEmpty()) {
                            notifications.forEach { message ->
                                DropdownMenuItem(onClick = {}) {
                                    Text(
                                        text = message,
                                        style = MaterialTheme.typography.body1,
                                        modifier = Modifier.padding(4.dp)
                                    )
                                }
                            }
                        } else {
                            DropdownMenuItem(onClick = {}) {
                                Text(
                                    text = "No notifications available",
                                    style = MaterialTheme.typography.body1,
                                    modifier = Modifier.padding(4.dp)
                                )
                            }
                        }

                        // "View My Notifications" Button at the Bottom
                        DropdownMenuItem(onClick = {
                            notificationExpanded = false
                            navController.navigate("my_notification")
                        }) {
                            Text(
                                text = "View My Notifications",
                                style = MaterialTheme.typography.body1,
                                color = MaterialTheme.colors.primary
                            )
                        }
                    }
                }

                // Existing menu
                Box {
                    IconButton(onClick = { expanded = true }) {
                        Icon(Icons.Filled.Menu, contentDescription = "Menu", tint = Color.White)
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(onClick = {
                            expanded = false
                            navController.navigate("user_profile")
                        }) {
                            Text("User Profile")
                        }

                        DropdownMenuItem(onClick = {
                            expanded = false
                            navController.navigate("settings")
                        }) {
                            Text("Settings")
                        }

                        DropdownMenuItem(onClick = {
                            expanded = false
                            navController.navigate("about_us")
                        }) {
                            Text("About Us")
                        }

                        DropdownMenuItem(onClick = {
                            expanded = false
                            navController.navigate("my_activities")
                        }) {
                            Text("My Activities")
                        }

                        DropdownMenuItem(onClick = {
                            expanded = false
                            val token = sharedPreferences.getString("authToken", null)
                            if (token != null) {
                                apiService.logoutUser("Bearer $token").enqueue(object : Callback<ResponseBody> {
                                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                                        if (response.isSuccessful) {
                                            sharedPreferences.edit().clear().apply()
                                            navController.navigate("login") {
                                                popUpTo("home") { inclusive = true }
                                            }
                                            Toast.makeText(context, "Logged out successfully", Toast.LENGTH_SHORT).show()
                                        } else {
                                            Toast.makeText(context, "Failed to log out", Toast.LENGTH_SHORT).show()
                                        }
                                    }

                                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                                        Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                                    }
                                })
                            }
                        }) {
                            Text("Logout")
                        }
                    }
                }

            }
        }


        Spacer(modifier = Modifier.height(8.dp))

        // City Input Field
        OutlinedTextField(
            value = cityInput,
            onValueChange = { cityInput = it },
            label = { Text("Enter City") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.White,
                unfocusedBorderColor = Color.White,
                textColor = Color.White,
                cursorColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Enter Button
        Button(
            onClick = {
                city = cityInput // Update the city state
                saveCityToPreferences(city) // Save the city to SharedPreferences
            },
            modifier = Modifier.align(Alignment.CenterHorizontally),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF2D9CDB))
        ) {
            Text("Enter", color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Location & Date
        if (weatherData != null) {
            Text(
                text = "${city.capitalize()}",
                color = Color.White,
                fontSize = 18.sp
            )
            Text(
                text = "${weatherData!!.date}, ${weatherData!!.dayOfWeek}",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 14.sp
            )
        } else if (errorMessage != null) {
            Text(
                text = errorMessage!!,
                color = Color.Red,
                fontSize = 16.sp
            )
        } else if (isLoading) {
            Text(
                text = "Loading location...",
                color = Color.White,
                fontSize = 18.sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Weather Display
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            contentAlignment = Alignment.Center
        ) {
            when {
                isLoading -> {
                    CircularProgressIndicator(color = Color.White)
                }
                errorMessage != null -> {
                    Text(errorMessage!!, color = Color.Red, fontSize = 16.sp)
                }
                weatherData != null -> {
                    Box(
                        modifier = Modifier
                            .background(Color(0xFF87CEEB), shape = RoundedCornerShape(16.dp))
                            .padding(horizontal = 50.dp, vertical = 16.dp)
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                weatherData!!.weatherDescription,
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                "Max: ${weatherData!!.maxTemp}°C",
                                fontSize = 20.sp,
                                color = Color.White
                            )
                            Text(
                                "Min: ${weatherData!!.minTemp}°C",
                                fontSize = 20.sp,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                navController.navigate("view_weekly_forecast/$city")
            },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
        ) {
            Text("View Weekly Forecast")
        }

        // Wardrobe Section//////////////////////////////////////////////////////////////////////////////////
        Text("Wardrobe", color = Color.Black, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            IconCard(R.drawable.shirt)
            IconCard(R.drawable.pants)
            IconCard(R.drawable.coat)
        }
        if (wardrobeData != null) {
            // Display the theme at the top
            Text(
                text = wardrobeData!!.theme,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            // Create a separate card for each clothing item and description
            wardrobeData!!.clothingItems.zip(wardrobeData!!.clothingDescriptions).forEach { (item, description) ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    shape = RoundedCornerShape(12.dp),
                    elevation = 4.dp
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = item,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = description,
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                }
            }
        } else {
            Text("Fetching wardrobe recommendation...", color = Color.Gray)
        }


        Button(
            onClick = {
                navController.navigate("view_all_wardrobe/$city")
            },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
        ) {
            Text("View More Wardrobes")
        }


        Spacer(modifier = Modifier.height(24.dp))
        Spacer(modifier = Modifier.height(24.dp))

        // Activities Section//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        Text("Activities", color = Color.Black, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            IconCard(R.drawable.cycling)
            IconCard(R.drawable.coat)
            IconCard(R.drawable.fishing)
        }
        if (activityData.isNotEmpty()) {
            activityData.forEach { activity ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    shape = RoundedCornerShape(12.dp),
                    elevation = 4.dp
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
                    }
                }
            }
        } else {
            Text("Fetching activity recommendations...", color = Color.Gray)
        }
        Button(
            onClick = {
                navController.navigate("view_all_activities/$city")
            },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
        ) {
            Text("View More Activities")
        }


        Spacer(modifier = Modifier.height(32.dp))

    }
}



@Composable
fun IconCard(iconResId: Int) {
    Box(
        modifier = Modifier
            .size(64.dp)
            .background(Color.Cyan.copy(alpha = 0.6f), shape = RoundedCornerShape(12.dp))
            .padding(8.dp)
    ) {
        Image(
            painter = painterResource(id = iconResId),
            contentDescription = "icon",
            modifier = Modifier.fillMaxSize()
        )
    }
    Spacer(modifier = Modifier.height(32.dp))
}

