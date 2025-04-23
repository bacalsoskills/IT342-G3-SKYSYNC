package com.frontend.mobile.viewactivities

import android.content.Context
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
import com.frontend.mobile.model.WardrobeRecommendation
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun ViewAllRecommendedWardrobes(
    city: String, // Pass the city as a parameter
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

    // Fetch all wardrobe recommendations
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

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Recommended Wardrobes", fontSize = 20.sp) },
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
                wardrobeRecommendations.forEachIndexed { index, wardrobe ->
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
                            // Display the theme
                            Text(
                                text = "Wardrobe ${index + 1}: ${wardrobe.theme}",
                                fontSize = 18.sp,
                                color = Color.Black,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            // Display clothing items and descriptions
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
