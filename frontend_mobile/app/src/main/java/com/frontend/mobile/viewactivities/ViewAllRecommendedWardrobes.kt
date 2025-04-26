package com.frontend.mobile.viewactivities

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.frontend.mobile.model.WardrobeRecommendation
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun ViewAllRecommendedWardrobes(
    city: String,
    navController: NavHostController,
    onBackClick: () -> Unit
) {
    val apiService = ApiClient.getClient().create(ApiService::class.java)
    val scrollState = rememberScrollState()
    var wardrobeRecommendations by remember { mutableStateOf<List<WardrobeRecommendation>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    val token = sharedPreferences.getString("authToken", null)

    LaunchedEffect(city) {
        if (token != null) {
            isLoading = true
            apiService.getTodayWardrobeByCity(city, "Bearer $token").enqueue(object : Callback<List<WardrobeRecommendation>> {
                override fun onResponse(
                    call: Call<List<WardrobeRecommendation>>,
                    response: Response<List<WardrobeRecommendation>>
                ) {
                    if (response.isSuccessful) {
                        wardrobeRecommendations = response.body() ?: emptyList()
                        isLoading = false
                    } else {
                        errorMessage = "Error: ${response.errorBody()?.string() ?: "Unknown error"}"
                        isLoading = false
                    }
                }

                override fun onFailure(call: Call<List<WardrobeRecommendation>>, t: Throwable) {
                    errorMessage = "Error: ${t.message}"
                    isLoading = false
                }
            })
        } else {
            errorMessage = "Authentication token not found"
            isLoading = false
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFA7F0F9))
    ) {
        // Back button
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .padding(top = 40.dp, start = 16.dp)
                .align(Alignment.TopStart)
        ) {
            Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
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

        // Content Column
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 200.dp, start = 20.dp, end = 20.dp, bottom = 16.dp)  // Adjusted padding
                .verticalScroll(scrollState)
                .align(Alignment.TopCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Title Section
            Text(
                text = "Recommended Wardrobes",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            when {
                isLoading -> {
                    CircularProgressIndicator()
                }

                errorMessage != null -> {
                    Text(
                        text = errorMessage!!,
                        color = Color.Red,
                        fontSize = 16.sp
                    )
                }

                else -> {
                    wardrobeRecommendations.forEachIndexed { index, wardrobe ->
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD)),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "Wardrobe ${index + 1}: ${wardrobe.theme}",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                                Spacer(modifier = Modifier.height(8.dp))

                                wardrobe.clothingItems.zip(wardrobe.clothingDescriptions).forEach { (item, description) ->
                                    Text(
                                        text = item,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Black
                                    )
                                    Text(
                                        text = "- $description",
                                        fontSize = 14.sp,
                                        color = Color.Gray
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
