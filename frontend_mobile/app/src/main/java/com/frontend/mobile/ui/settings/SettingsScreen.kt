package com.frontend.mobile.ui.settings

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
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
import com.frontend.mobile.model.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun SettingsScreen(
    navController: NavHostController,
    onBack: () -> Unit
) {
    var pushNotificationsEnabled by remember { mutableStateOf(true) }
    var user by remember { mutableStateOf<User?>(null) }
    val apiService = ApiClient.getClient().create(ApiService::class.java)
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    val userId = sharedPreferences.getLong("userId", -1L)
    val token = sharedPreferences.getString("authToken", null)

    // Fetch user data
    LaunchedEffect(Unit) {
        if (userId != -1L && token != null) {
            apiService.getUserDetails(userId, "Bearer $token").enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if (response.isSuccessful) {
                        user = response.body()
                    } else {
                        Toast.makeText(context, "Failed to fetch user details", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    Box(

        modifier = Modifier

            .fillMaxSize()
            .background(Color(0xFFA7F0F9))
    )


    {

        // Back Arrow
        IconButton(
            onClick = onBack,
            modifier = Modifier
                .padding(top = 40.dp)  // Increase top padding here
                .padding(start = 16.dp)
                .align(Alignment.TopStart)
        ) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
        }
        Spacer(modifier = Modifier.height(100.dp))
        // Logo
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 70.dp, start = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally


        ) {
            Image(
                painter = painterResource(id = R.drawable.imagelogo),
                contentDescription = "SkySync Logo",
                modifier = Modifier.height(100.dp)
            )
        }

        // Settings Card
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
                .padding(top = 190.dp)

                .background(Color(0xFFA7F0F9))
                .padding(16.dp)
                .padding(top = 20.dp)
                .align(Alignment.TopCenter)
        ) {
            // Header Title
            Text(
                text = "Settings",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 12.dp)
            )

            // Display user name if available
            user?.let { currentUser ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "User Icon",
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("${currentUser.firstName} ${currentUser.lastName}", fontSize = 18.sp, fontWeight = FontWeight.Medium)
                }
            }

            Text("Account Settings", color = Color.Gray, fontSize = 14.sp)

            // 👉 Navigate to UserProfile Screen
            SettingsItem("Edit profile") {
                navController.navigate("user_profile")
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Push notifications", fontSize = 16.sp)
                Switch(
                    checked = pushNotificationsEnabled,
                    onCheckedChange = { pushNotificationsEnabled = it }
                )
            }

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            Text("More", color = Color.Gray, fontSize = 14.sp)

            SettingsItem("About us") {
                navController.navigate("about_us") // Optional screen route
            }

        }
    }
}

@Composable
fun SettingsItem(title: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = title, fontSize = 16.sp)
        Icon(
            painter = painterResource(id = R.drawable.imagecloud), // Replace with right-arrow icon
            contentDescription = null,
            tint = Color.Gray
        )
    }
}
