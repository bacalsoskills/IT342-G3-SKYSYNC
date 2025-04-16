package com.frontend.frontend

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.frontend.R
import com.frontend.frontend.api.ApiClient
import com.frontend.frontend.model.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val emailEditText = findViewById<EditText>(R.id.Email)
        val passwordEditText = findViewById<EditText>(R.id.Password)
        val loginButton = findViewById<ImageView>(R.id.login)
        val registerImageView = findViewById<ImageView>(R.id.Register)

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                val credentials = mapOf("email" to email, "password" to password)
                ApiClient.apiService.loginUser(credentials).enqueue(object : Callback<User> {
                    override fun onResponse(call: Call<User>, response: Response<User>) {
                        if (response.isSuccessful && response.body() != null) {
                            // If login is successful, proceed to the HomePage
                            startActivity(Intent(this@LoginActivity, HomePage::class.java))
                            finish() // Optionally close the LoginActivity to prevent back navigation
                        } else {
                            // If credentials are incorrect, show an error message
                            Toast.makeText(this@LoginActivity, "Invalid credentials", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<User>, t: Throwable) {
                        // Show error message if the request fails
                        Toast.makeText(this@LoginActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
                // Show message if either email or password is missing
                Toast.makeText(this, "Please enter both email and password", Toast.LENGTH_SHORT).show()
            }
        }

        // Navigate to the RegisterActivity when the register image is clicked
        registerImageView.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}
