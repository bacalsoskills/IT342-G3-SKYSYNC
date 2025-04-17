package com.frontend.mobile.ui.settings

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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.frontend.mobile.R

@Composable
fun SettingsScreen(
    navController: NavHostController,
    onBack: () -> Unit
) {
    var pushNotificationsEnabled by remember { mutableStateOf(true) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFA7F0F9))
    ) {
        // Back Arrow
        IconButton(
            onClick = onBack,
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.TopStart)
        ) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
        }

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
                .clip(RoundedCornerShape(16.dp))
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
                Text("John Pork", fontSize = 18.sp, fontWeight = FontWeight.Medium)
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

            SettingsItem("Privacy policy") {
                navController.navigate("privacy_policy") // Optional screen route
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
