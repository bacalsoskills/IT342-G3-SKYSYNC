package com.frontend.mobile.ui.loginpage

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.frontend.mobile.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.frontend.mobile.api.ApiClient
import com.frontend.mobile.api.ApiService
import com.frontend.mobile.model.AuthResponse
import android.widget.Toast
import android.content.Context

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit, // Navigate to the next screen
    onRegisterClick: () -> Unit = {}
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    val apiService = ApiClient.getClient().create(ApiService::class.java)

    val gradientBackground = Brush.verticalGradient(
        colors = listOf(Color(0xFFB2FEFA), Color(0xFF0ED2F7))
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBackground),
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Logo
            Image(
                painter = painterResource(id = R.drawable.imagelogo),
                contentDescription = "SkySync Logo",
                modifier = Modifier.height(140.dp)
            )

            // Title
            Text(
                text = "Sign in",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF3F51B5),
                modifier = Modifier.padding(vertical = 12.dp)
            )

            // Email Input
            Text("Email", fontSize = 14.sp, color = Color.Gray, modifier = Modifier.padding(start = 8.dp))
            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    emailError = false
                },
                singleLine = true,
                isError = emailError,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .background(Color.White, RoundedCornerShape(10.dp)),
            )
            if (emailError) {
                Text("Please enter a valid email", color = Color.Red, fontSize = 12.sp)
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Password Input
            Text("Password", fontSize = 14.sp, color = Color.Gray, modifier = Modifier.padding(start = 8.dp))
            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    passwordError = false
                },
                singleLine = true,
                isError = passwordError,
                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 1.dp)
                    .background(Color.White, RoundedCornerShape(10.dp)),
                trailingIcon = {
                    Text(
                        if (showPassword) "Hide" else "Show",
                        color = Color(0xFF3F51B5),
                        fontSize = 12.sp,
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .clickable { showPassword = !showPassword }
                    )
                }
            )
            if (passwordError) {
                Text("Password must not be empty", color = Color.Red, fontSize = 12.sp)
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Login Button
            Button(
                onClick = {
                    emailError = email.isBlank()
                    passwordError = password.isBlank()

                    if (!emailError && !passwordError) {
                        val loginRequest = mapOf("email" to email, "password" to password)
                        apiService.loginUser(loginRequest).enqueue(object : Callback<AuthResponse> {
                            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                                if (response.isSuccessful) {
                                    val authResponse = response.body()
                                    authResponse?.let {
                                        // Save token and userId in SharedPreferences
                                        sharedPreferences.edit()
                                            .putString("authToken", it.token)
                                            .putLong("userId", it.userId)
                                            .apply()

                                        Toast.makeText(context, "Welcome, ${it.email}", Toast.LENGTH_SHORT).show()
                                        onLoginSuccess() // Navigate to the next screen
                                    }
                                } else {
                                    Toast.makeText(context, "Invalid credentials", Toast.LENGTH_SHORT).show()
                                }
                            }

                            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                                Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                            }
                        })
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF2979FF)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Log in", color = Color.White, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))
            Divider(color = Color.LightGray, thickness = 1.dp)
            Spacer(modifier = Modifier.height(8.dp))

            // Register Navigation
            Text(
                text = "Don't have an account?",
                color = Color.DarkGray,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Button(
                onClick = onRegisterClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF3F51B5)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Register", color = Color.White, fontSize = 16.sp)
            }
        }
    }
}
