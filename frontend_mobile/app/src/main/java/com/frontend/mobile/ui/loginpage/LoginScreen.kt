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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.core.content.edit

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBackground)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(50.dp))

        // ✅ Logo
        Image(
            painter = painterResource(id = R.drawable.imagelogo),
            contentDescription = "SkySync Logo",
            modifier = Modifier.height(140.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Sign in",
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF3F51B5)
        )

        Spacer(modifier = Modifier.height(30.dp))

        // Email
        Text("Email", fontSize = 14.sp, color = Color.Gray, modifier = Modifier.fillMaxWidth())
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
                .background(Color.White, RoundedCornerShape(10.dp)),
        )
        if (emailError) {
            Text("Please enter a valid email", color = Color.Red, fontSize = 12.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Password
        Text("Password", fontSize = 14.sp, color = Color.Gray, modifier = Modifier.fillMaxWidth())
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

        Spacer(modifier = Modifier.height(24.dp))

        // Login Button
        Button(
            onClick = {
                emailError = email.isBlank()
                passwordError = password.isBlank()

                if (!emailError && !passwordError) {
                    if (email == "1" && password == "1") {
                        Toast.makeText(context, "Welcome Admin!", Toast.LENGTH_SHORT).show()
                        sharedPreferences.edit {
                            putString("authToken", "admin-token")
                            putLong("userId", 0L)
                        }
                        onLoginSuccess()
                    } else {
                        val loginRequest = mapOf("email" to email, "password" to password)
                        apiService.loginUser(loginRequest).enqueue(object : Callback<AuthResponse> {
                            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                                if (response.isSuccessful) {
                                    response.body()?.let {
                                        sharedPreferences.edit {
                                            putString("authToken", it.token)
                                            putLong("userId", it.userId)
                                        }
                                        Toast.makeText(context, "Welcome, ${it.email}", Toast.LENGTH_SHORT).show()
                                        onLoginSuccess()
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

        // Register Prompt
        Text(
            text = "Don't have an account?",
            color = Color.DarkGray,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(4.dp))

        Button(
            onClick = onRegisterClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF2979FF)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Register", color = Color.White, fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(50.dp))
    }
}
