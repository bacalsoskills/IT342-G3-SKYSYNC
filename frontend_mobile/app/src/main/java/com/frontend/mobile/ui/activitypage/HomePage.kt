package com.frontend.mobile.ui.activitypage

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.frontend.mobile.R

@Composable
fun HomePage(navController: NavHostController) {
    val gradientBackground = Brush.verticalGradient(
        colors = listOf(Color(0xFFB2FEFA), Color(0xFF0ED2F7))
    )

    val scrollState = rememberScrollState()
    var expanded by remember { mutableStateOf(false) }

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
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Location & Date
        Text("Philippines, Cebu City", color = Color.White, fontSize = 18.sp)
        Text("Cebu, 3 March 2025", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)

        Spacer(modifier = Modifier.height(16.dp))

        // Weather Display
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .background(Color(0xFF87CEEB), shape = RoundedCornerShape(16.dp))
                    .padding(horizontal = 50.dp, vertical = 16.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("30°C", fontSize = 48.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Text("Cloudy", fontSize = 20.sp, color = Color.White)
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
        Spacer(modifier = Modifier.height(8.dp))
        ViewMoreButton {

        }

        Spacer(modifier = Modifier.height(24.dp))

        // Activities Section
        Text("Activities", color = Color.Black, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            IconCard(R.drawable.walking)
            IconCard(R.drawable.cycling)
            IconCard(R.drawable.fishing)
        }
        Spacer(modifier = Modifier.height(8.dp))
        ViewMoreButton {
            // Navigate to WeatherActivityPage with a weather code
            navController.navigate("weather_activities/1") // Example weatherCode: 1
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
