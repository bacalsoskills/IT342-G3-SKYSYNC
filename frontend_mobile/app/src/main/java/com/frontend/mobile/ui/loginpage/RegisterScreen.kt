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
        return input.isNotEmpty() && input.contains("@gmail.com")
    }

    // Function to validate password strength
    fun isStrongPassword(input: String): Boolean {
        return input.length >= 8 &&
                input.any { it.isLowerCase() } &&
                input.any { it.isUpperCase() } &&
                input.any { it.isDigit() }
    }

    // Password strength checker
    fun passwordStrength(password: String): String {
        return when {
            password.length < 8 -> "Weak (Too Short)"
            password.none { it.isUpperCase() } || password.none { it.isDigit() } -> "Weak (Missing Uppercase/Number)"
            password.length in 8..12 -> "Medium"
            else -> "Strong"
        }
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
        // Scrollable Column for input fields and buttons
        Column(
            modifier = Modifier
                .padding(top = 60.dp, start = 24.dp, end = 24.dp)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()), // Add vertical scroll here
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Logo
            Image(
                painter = painterResource(id = R.drawable.imagelogo),
                contentDescription = "SkySync Logo",
                modifier = Modifier
                    .height(140.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Register title with blue text
            Text(
                text = "Register",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF003C8F), // Dark blue color
                modifier = Modifier.padding(vertical = 8.dp)
            )

            // Input Fields with labels above each field
            Column(modifier = Modifier.fillMaxWidth()) {
                // First Name
                Text("First Name", fontSize = 14.sp, color = Color(0xFF003C8F), modifier = Modifier.padding(start = 8.dp)) // Blue text
                OutlinedTextField(
                    value = firstName,
                    onValueChange = {
                        firstName = capitalizeName(it)
                    },
                    singleLine = true,
                    isError = firstNameError,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .background(Color.White, RoundedCornerShape(10.dp))
                )
                if (firstNameError) {
                    Text("First Name must start with a capital letter", color = Color.Red, fontSize = 12.sp)
                }

                // Last Name
                Text("Last Name", fontSize = 14.sp, color = Color(0xFF003C8F), modifier = Modifier.padding(start = 8.dp)) // Blue text
                OutlinedTextField(
                    value = lastName,
                    onValueChange = {
                        lastName = capitalizeName(it)
                    },
                    singleLine = true,
                    isError = lastNameError,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .background(Color.White, RoundedCornerShape(10.dp))
                )
                if (lastNameError) {
                    Text("Last Name must start with a capital letter", color = Color.Red, fontSize = 12.sp)
                }

                // Email
                Text("Email", fontSize = 14.sp, color = Color(0xFF003C8F), modifier = Modifier.padding(start = 8.dp)) // Blue text
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    singleLine = true,
                    isError = emailError,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .background(Color.White, RoundedCornerShape(10.dp))
                )
                if (emailError) {
                    Text("Please use a valid Gmail address", color = Color.Red, fontSize = 12.sp)
                }

                // Password
                Text("Password", fontSize = 14.sp, color = Color(0xFF003C8F), modifier = Modifier.padding(start = 8.dp)) // Blue text
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    singleLine = true,
                    isError = passwordError,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .background(Color.White, RoundedCornerShape(10.dp)),
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = "Toggle Password Visibility",
                                tint = Color(0xFF003C8F) // Set icon color to blue
                            )
                        }
                    }
                )

                // Password Strength Indicator
                val passwordStrength = passwordStrength(password)
                Text(
                    text = "Password Strength: $passwordStrength",
                    color = when (passwordStrength) {
                        "Weak" -> Color.Red
                        "Medium" -> Color.Yellow
                        "Strong" -> Color.Green
                        else -> Color.Gray
                    },
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )

                if (passwordError) {
                    Text("Password must be at least 8 characters long, contain uppercase, lowercase, and a number", color = Color.Red, fontSize = 12.sp)
                }

                // Confirm Password
                Text("Confirm Password", fontSize = 14.sp, color = Color(0xFF003C8F), modifier = Modifier.padding(start = 8.dp)) // Blue text
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    singleLine = true,
                    isError = confirmPasswordError,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .background(Color.White, RoundedCornerShape(10.dp)),
                    trailingIcon = {
                        IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                            Icon(
                                imageVector = if (confirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = "Toggle Password Visibility",
                                tint = Color(0xFF003C8F) // Set icon color to blue
                            )
                        }
                    }
                )
                if (confirmPasswordError) {
                    Text("Passwords do not match", color = Color.Red, fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Register Button with blue color
            Button(
                onClick = {
                    // Perform validation
                    emailError = !isValidEmail(email)
                    passwordError = !isStrongPassword(password)
                    firstNameError = firstName.isEmpty() || firstName[0].isLowerCase()
                    lastNameError = lastName.isEmpty() || lastName[0].isLowerCase()
                    confirmPasswordError = confirmPassword != password

                    // If all validations pass, proceed with registration
                    if (!emailError && !passwordError && !firstNameError && !lastNameError && !confirmPasswordError) {
                        // Handle successful registration (e.g., make network call)

                        // After successful registration, navigate to the login screen
                        onBackToLogin()  // This will trigger the navigation to the login page
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .shadow(4.dp, RoundedCornerShape(10.dp)),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xFF0091EA), // Blue button background
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Register", fontSize = 16.sp, fontWeight = FontWeight.Medium)
            }
            // Back to Login Button with blue color
            Spacer(modifier = Modifier.height(16.dp))
            TextButton(
                onClick = { onBackToLogin() }
            ) {
                Text("Back to Login", color = Color(0xFF003C8F), fontSize = 14.sp, textAlign = TextAlign.Center) // Blue text
            }

            Spacer(modifier = Modifier.height(80.dp)) // To ensure scrollability
        }
    }
}
