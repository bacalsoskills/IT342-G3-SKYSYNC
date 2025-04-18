package com.frontend.mobile.ui.loginpage

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.frontend.mobile.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.frontend.mobile.api.ApiClient
import com.frontend.mobile.api.ApiService
import com.frontend.mobile.model.User

@Composable
fun RegisterScreen(onBackToLogin: () -> Unit) {
    // State variables for inputs
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    // Validation States
    var emailError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }
    var firstNameError by remember { mutableStateOf(false) }
    var lastNameError by remember { mutableStateOf(false) }
    var confirmPasswordError by remember { mutableStateOf(false) }

    // Password visibility toggle
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    // Function to capitalize first and last name properly
    fun capitalizeName(name: String): String {
        return name.split(" ")
            .joinToString(" ") { it.capitalize() }
    }

    // Function to validate email (check if it's a valid Gmail address)
    fun isValidEmail(input: String): Boolean {
        return input.isNotEmpty() && input.contains("@")
    }

    // Function to validate password strength
    fun isStrongPassword(input: String): Boolean {
        return input.length >= 8 &&
                input.any { it.isLowerCase() } &&
                input.any { it.isUpperCase() } &&
                input.any { it.isDigit() }
    }

    // Background gradient with blue color
    val backgroundBrush = Brush.verticalGradient(
        colors = listOf(Color(0xFFB2FEFA), Color(0xFF0ED2F7))
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = backgroundBrush),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .padding(top = 60.dp, start = 24.dp, end = 24.dp)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Logo
            Image(
                painter = painterResource(id = R.drawable.imagelogo),
                contentDescription = "SkySync Logo",
                modifier = Modifier.height(140.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Register title
            Text(
                text = "Register",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF003C8F),
                modifier = Modifier.padding(vertical = 8.dp)
            )

            // Input Fields
            Column(modifier = Modifier.fillMaxWidth()) {
                // First Name
                OutlinedTextField(
                    value = firstName,
                    onValueChange = { firstName = capitalizeName(it) },
                    label = { Text("First Name") },
                    isError = firstNameError,
                    modifier = Modifier.fillMaxWidth()
                )
                if (firstNameError) {
                    Text("First Name must not be empty", color = Color.Red, fontSize = 12.sp)
                }

                // Last Name
                OutlinedTextField(
                    value = lastName,
                    onValueChange = { lastName = capitalizeName(it) },
                    label = { Text("Last Name") },
                    isError = lastNameError,
                    modifier = Modifier.fillMaxWidth()
                )
                if (lastNameError) {
                    Text("Last Name must not be empty", color = Color.Red, fontSize = 12.sp)
                }

                // Email
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    isError = emailError,
                    modifier = Modifier.fillMaxWidth()
                )
                if (emailError) {
                    Text("Please enter a valid email", color = Color.Red, fontSize = 12.sp)
                }

                // Password
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = "Toggle Password Visibility"
                            )
                        }
                    },
                    isError = passwordError,
                    modifier = Modifier.fillMaxWidth()
                )
                if (passwordError) {
                    Text("Password must be at least 8 characters long and contain uppercase, lowercase, and a number", color = Color.Red, fontSize = 12.sp)
                }

                // Confirm Password
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text("Confirm Password") },
                    visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                            Icon(
                                imageVector = if (confirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = "Toggle Confirm Password Visibility"
                            )
                        }
                    },
                    isError = confirmPasswordError,
                    modifier = Modifier.fillMaxWidth()
                )
                if (confirmPasswordError) {
                    Text("Passwords do not match", color = Color.Red, fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            val apiService = ApiClient.getClient().create(ApiService::class.java)

            // Register Button
            Button(
                onClick = {
                    // Perform validation
                    emailError = !isValidEmail(email)
                    passwordError = !isStrongPassword(password)
                    firstNameError = firstName.isEmpty()
                    lastNameError = lastName.isEmpty()
                    confirmPasswordError = confirmPassword != password

                    if (!emailError && !passwordError && !firstNameError && !lastNameError && !confirmPasswordError) {
                        val user = User(
                            firstName = firstName,
                            lastName = lastName,
                            email = email,
                            password = password
                        )
                        apiService.registerUser(user).enqueue(object : Callback<User> {
                            override fun onResponse(call: Call<User>, response: Response<User>) {
                                if (response.isSuccessful) {
                                    onBackToLogin() // Navigate to login screen
                                } else {
                                    // Handle error
                                    println("Error: ${response.errorBody()?.string()}")
                                }
                            }

                            override fun onFailure(call: Call<User>, t: Throwable) {
                                // Handle failure
                                println("Failure: ${t.message}")
                            }
                        })
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Register")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Back to Login Button
            TextButton(onClick = { onBackToLogin() }) {
                Text("Back to Login")
            }
        }
    }
}
