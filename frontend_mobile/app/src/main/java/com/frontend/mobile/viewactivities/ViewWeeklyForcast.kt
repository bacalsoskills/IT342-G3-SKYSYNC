package com.frontend.mobile.viewactivities

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.frontend.mobile.api.ApiClient
import com.frontend.mobile.api.ApiService
import com.frontend.mobile.model.DailyWeatherDTO
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun ViewWeeklyForecast(city: String, navController: NavHostController) {
    val apiService = ApiClient.getClient().create(ApiService::class.java)
    val scrollState = rememberScrollState()
    var weeklyForecast by remember { mutableStateOf<List<DailyWeatherDTO>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Fetch weekly forecast
    LaunchedEffect(city) {
        apiService.getWeeklyWeatherByCity(city).enqueue(object : Callback<List<DailyWeatherDTO>> {
            override fun onResponse(
                call: Call<List<DailyWeatherDTO>>,
                response: Response<List<DailyWeatherDTO>>
            ) {
                if (response.isSuccessful) {
                    weeklyForecast = response.body() ?: emptyList()
                    isLoading = false
                } else {
                    errorMessage = "Error: ${response.errorBody()?.string() ?: "Unknown error"}"
                    isLoading = false
                }
            }

            override fun onFailure(call: Call<List<DailyWeatherDTO>>, t: Throwable) {
                errorMessage = "Error: ${t.message}"
                isLoading = false
            }
        })
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        // Back Arrow
        IconButton(
            onClick = { navController.popBackStack() }, // Navigates back to the HomePage
            modifier = Modifier
                .padding(top = 16.dp, start = 8.dp)
                .align(Alignment.Start)
        ) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Weekly Forecast for $city",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(16.dp))

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
            weeklyForecast.forEach { dailyWeather ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    elevation = 4.dp
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = dailyWeather.date,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = dailyWeather.weatherDescription,
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Max Temp: ${dailyWeather.maxTemp}°C",
                            fontSize = 14.sp,
                            color = Color.Black
                        )
                        Text(
                            text = "Min Temp: ${dailyWeather.minTemp}°C",
                            fontSize = 14.sp,
                            color = Color.Black
                        )
                    }
                }
            }
        }
    }
}