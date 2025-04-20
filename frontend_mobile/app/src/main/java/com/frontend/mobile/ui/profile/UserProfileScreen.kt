package com.frontend.mobile.ui.profile

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.frontend.mobile.api.ApiClient
import com.frontend.mobile.api.ApiService
import com.frontend.mobile.model.AuthResponse
import com.frontend.mobile.model.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun UserProfileScreen(navController: NavController) {
    var user by remember { mutableStateOf<User?>(null) }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") } // Add confirmation field
    val apiService = ApiClient.getClient().create(ApiService::class.java)
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    val userId = sharedPreferences.getLong("userId", -1L)
    val token = sharedPreferences.getString("authToken", null)

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

    val gradientBackground = Brush.verticalGradient(
        colors = listOf(Color(0xFFB2FEFA), Color(0xFF0ED2F7))
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBackground),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 80.dp)
        ) {
            Text("User Profile", fontSize = 16.sp, color = Color.DarkGray)

            user?.let { currentUser ->
                ProfileTextField(
                    label = "First Name",
                    value = currentUser.firstName,
                    onValueChange = { newValue -> user = currentUser.copy(firstName = newValue) }
                )
                ProfileTextField(
                    label = "Last Name",
                    value = currentUser.lastName,
                    onValueChange = { newValue -> user = currentUser.copy(lastName = newValue) }
                )
                ProfileTextField(
                    label = "Email",
                    value = currentUser.email,
                    onValueChange = { newValue -> user = currentUser.copy(email = newValue) }
                )
                ProfileTextField(
                    label = "New Password",
                    value = newPassword,
                    onValueChange = { newValue -> newPassword = newValue },
                    isPassword = true
                )
                ProfileTextField(
                    label = "Confirm Password",
                    value = confirmPassword,
                    onValueChange = { newValue -> confirmPassword = newValue },
                    isPassword = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (newPassword.isNotBlank() && newPassword != confirmPassword) {
                            Toast.makeText(context, "Passwords don't match", Toast.LENGTH_SHORT).show()
                            return@Button
                        }

                        val updatedUser = user ?: return@Button

                        // Only include password in the update if it was changed
                        val userUpdateRequest = if (newPassword.isNotBlank()) {
                            updatedUser.copy(password = newPassword)
                        } else {
                            updatedUser.copy(password = null) // Or however your API expects no password change
                        }

                        apiService.updateUserDetails(userId, "Bearer $token", userUpdateRequest).enqueue(object : Callback<AuthResponse> {
                            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                                if (response.isSuccessful) {
                                    val authResponse = response.body()
                                    authResponse?.let {
                                        // Save the new token in SharedPreferences
                                        sharedPreferences.edit()
                                            .putString("authToken", it.token)
                                            .apply()

                                        Toast.makeText(context, "Profile updated successfully!", Toast.LENGTH_SHORT).show()
                                        newPassword = ""
                                        confirmPassword = ""
                                    }
                                } else {
                                    Toast.makeText(context, "Failed to update profile", Toast.LENGTH_SHORT).show()
                                }
                            }

                            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                                Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                            }
                        })
                    },
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
}

@Composable
fun ProfileTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    leadingIcon: @Composable (() -> Unit)? = null,
    isPassword: Boolean = false
) {
    Column(modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)) {
        Text(text = label, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            leadingIcon = leadingIcon,
            visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
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
