package com.frontend.frontend_mobile

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)

        // Set up the system window insets for padding
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val startNowImageView = findViewById<ImageView>(R.id.login)
        startNowImageView.setOnClickListener {
            if (isFormValid()) {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Please complete all fields and ensure passwords match", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Function to validate the form
    private fun isFormValid(): Boolean {
        val firstNameEditText = findViewById<EditText>(R.id.FirstName)
        val lastNameEditText = findViewById<EditText>(R.id.LastName)
        val emailEditText = findViewById<EditText>(R.id.Email)
        val passwordEditText = findViewById<EditText>(R.id.Password)
        val confirmPasswordEditText = findViewById<EditText>(R.id.editTextText5)

        // Check if any field is empty
        if (firstNameEditText.text.isEmpty() || lastNameEditText.text.isEmpty() || emailEditText.text.isEmpty() ||
            passwordEditText.text.isEmpty() || confirmPasswordEditText.text.isEmpty()) {
            return false
        }

        // Check if password and confirm password match
        if (passwordEditText.text.toString() != confirmPasswordEditText.text.toString()) {
            return false
        }

        // If all fields are filled and passwords match, return true
        return true
    }
}
