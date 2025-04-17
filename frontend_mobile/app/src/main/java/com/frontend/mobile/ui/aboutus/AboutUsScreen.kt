package com.frontend.mobile.ui.aboutus

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.frontend.mobile.R

@Composable
fun AboutUsScreen(onBackClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    listOf(Color(0xFFB3FFF2), Color(0xFFAAF7F1))
                )
            )
            .padding(16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier.fillMaxSize()
        ) {

            // Top Row: Back Icon
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { onBackClick() }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Logo Image
            Image(
                painter = painterResource(id = R.drawable.imagelogo),
                contentDescription = "SkySync Logo",
                modifier = Modifier
                    .height(140.dp)
                    .padding(vertical = 8.dp),
                contentScale = ContentScale.Fit
            )

            // About Us Title
            Text(
                text = "About Us",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF3B3BC2),
                modifier = Modifier.padding(vertical = 8.dp)
            )

            // Description
            Text(
                text = "At SkySync, we help you make the most of your day—rain or shine. Our smart outdoor activity planner and wardrobe assistant use real-time weather data to provide personalized activity and clothing recommendations. Whether you're heading out for an adventure or planning a cozy indoor day, SkySync ensures you're always prepared. With seamless web and mobile integration, we bring convenience, insights, and smarter planning to your daily routine.",
                fontSize = 14.sp,
                textAlign = TextAlign.Justify,
                color = Color.Black,
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Headquarters
            Text(
                text = "Headquarters",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF3B3BC2),
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Text(
                text = "Cebu Institute of Technology – University\nN. Bacalso Avenue\nCebu City, Philippines",
                fontSize = 13.sp,
                textAlign = TextAlign.Center,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Socials
            Text(
                text = "Social",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF3B3BC2),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Row(horizontalArrangement = Arrangement.Center) {
                val icons = listOf(
                    R.drawable.coat,
                    R.drawable.pants,
                    R.drawable.shirt,
                    R.drawable.cycling
                )
                icons.forEach {
                    Image(
                        painter = painterResource(id = it),
                        contentDescription = "Social Icon",
                        modifier = Modifier
                            .size(28.dp)
                            .padding(horizontal = 4.dp)
                    )
                }
            }
        }
    }
}
