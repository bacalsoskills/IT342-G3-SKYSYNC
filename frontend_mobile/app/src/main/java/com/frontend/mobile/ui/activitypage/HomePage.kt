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
    var cityInput by remember { mutableStateOf("Cebu") } // Default input is "Cebu"
    var city by remember { mutableStateOf("Cebu") } // Default city for fetching data

    // State to hold wardrobe data
    var wardrobeData by remember { mutableStateOf<WardrobeRecommendation?>(null) }

    // State to hold activity data
    var activityData by remember { mutableStateOf<List<ActivityDTO>>(emptyList()) }

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

    // Fetch wardrobe data
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
                        // Logout Functionality
                        val token = sharedPreferences.getString("authToken", null)
                        if (token != null) {
                            apiService.logoutUser("Bearer $token").enqueue(object : Callback<ResponseBody> {
                                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                                    if (response.isSuccessful) {
                                        // Clear SharedPreferences
                                        sharedPreferences.edit().clear().apply()
                                        // Navigate to Login Page
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
                        } else {
                            Toast.makeText(context, "No token found. Please log in again.", Toast.LENGTH_SHORT).show()
                            navController.navigate("login") {
                                popUpTo("home") { inclusive = true }
                            }
                        }
                    }) {
                        Text("Logout")
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
                city = cityInput // Update the city state to trigger the API call
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


        // Wardrobe Section
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
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                shape = RoundedCornerShape(12.dp),
                elevation = 4.dp
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = wardrobeData!!.theme,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Clothing Items:", fontWeight = FontWeight.Bold, color = Color.Black)
                    wardrobeData!!.clothingItems.forEach { item ->
                        Text("- $item", color = Color.Black)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Descriptions:", fontWeight = FontWeight.Bold, color = Color.Black)
                    wardrobeData!!.clothingDescriptions.forEach { description ->
                        Text("- $description", color = Color.Black)
                    }
                }
            }
        } else {
            Text("Fetching wardrobe recommendation...", color = Color.Gray)
        }

        Spacer(modifier = Modifier.height(8.dp))
        ViewMoreButton {

        }


        Spacer(modifier = Modifier.height(24.dp))

        // Activities Section
        Text("Activities", color = Color.Black, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))

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

        Spacer(modifier = Modifier.height(8.dp))
        ViewMoreButton {
            // Navigate to a detailed activity page if needed
            navController.navigate("activities")
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
}

@Composable
fun ViewMoreButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = onClick,
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF2D9CDB))
        ) {
            Text("View More", color = Color.White)
        }
    }
}
