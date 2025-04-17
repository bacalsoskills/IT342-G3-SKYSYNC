package com.frontend.mobile.ui.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.frontend.mobile.R

@Composable
fun UserProfileScreen(navController: NavController) {
    var firstName by remember { mutableStateOf("John") }
    var lastName by remember { mutableStateOf("Pork") }
    var email by remember { mutableStateOf("johnpork@gmail.com") }
    var password by remember { mutableStateOf("John Pork") }

    val gradientBackground = Brush.verticalGradient(
        colors = listOf(Color(0xFFB2FEFA), Color(0xFF0ED2F7))
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBackground),
    ) {
        // 🔙 Back Button to Homepage
        IconButton(
            onClick = {
                navController.navigate("home") {
                    popUpTo("profile") { inclusive = true } // Clears profile from backstack
                    launchSingleTop = true
                }
            },
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.TopStart)
        ) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 80.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.imagelogo),
                contentDescription = "Logo",
                modifier = Modifier
                    .height(100.dp)
                    .padding(top = 16.dp),
                contentScale = ContentScale.Fit
            )

            Text("User Profile", fontSize = 16.sp, color = Color.DarkGray)

            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "Avatar",
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray)
                    .padding(8.dp)
            )

            Text("John Pork", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text("johnpork@gmail.com", fontSize = 14.sp, color = Color.Gray)

            Spacer(modifier = Modifier.height(24.dp))

            // Form Fields
            ProfileTextField(
                label = "First Name",
                value = firstName,
                onValueChange = { firstName = it })
            ProfileTextField(
                label = "Last Name",
                value = lastName,
                onValueChange = { lastName = it })
            ProfileTextField(
                label = "Email",
                value = email,
                onValueChange = { email = it },
                leadingIcon = {
                    Image(
                        painter = painterResource(id = R.drawable.imagecloud),
                        contentDescription = "Cloud",
                        modifier = Modifier.size(24.dp)
                    )
                }
            )
            ProfileTextField(
                label = "Password",
                value = password,
                onValueChange = { password = it })

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { /* Handle update */ },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF0047FF)),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .padding(horizontal = 32.dp)
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Update Profile", color = Color.White, fontSize = 16.sp)
            }
        }
    }
}

@Composable
fun ProfileTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    leadingIcon: @Composable (() -> Unit)? = null
) {
    Column(modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)) {
        Text(text = label, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            leadingIcon = leadingIcon,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(10.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                backgroundColor = Color.White,
                focusedBorderColor = Color.LightGray,
                unfocusedBorderColor = Color.LightGray
            )
        )
    }
}
