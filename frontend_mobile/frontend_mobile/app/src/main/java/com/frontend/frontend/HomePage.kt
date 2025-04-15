package com.frontend.frontend

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
//import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.frontend.R

class HomePage : AppCompatActivity() {
     override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //enableEdgeToEdge()

        setContentView(R.layout.activity_home_page)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
         val startNowImageView = findViewById<ImageView>(R.id.imageView5)
         startNowImageView.setOnClickListener {
             val intent = Intent(this, LoginActivity::class.java)
             startActivity(intent)
         }
    }
}