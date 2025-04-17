package com.frontend.mobile.ui.activitypage

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.frontend.mobile.R

@Composable
fun WeatherActivityPage(weatherCode: Int, onBackClick: () -> Unit) {
    val (weatherDescription, activities, wardrobe) = getWeatherSuggestions(weatherCode)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Optional: Show a weather icon if desired
        Image(
            painter = painterResource(id = getWeatherIcon(weatherCode)),
            contentDescription = "Weather Icon",
            modifier = Modifier
                .size(100.dp)
                .padding(bottom = 8.dp)
        )

        Text(
            text = weatherDescription,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Suggested Activities:",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )
        activities.forEach {
            Text("• $it", fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Suggested Wardrobe:",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )
        wardrobe.forEach {
            Text("• $it", fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Button to navigate back
        Button(onClick = onBackClick) {
            Text("Go Back")
        }
    }
}

// Suggestions based on weather codes
fun getWeatherSuggestions(code: Int): Triple<String, List<String>, List<String>> {
    return when (code) {
        0 -> Triple(
            "Clear sky",
            listOf("Go for a walk", "Picnic in the park", "Cycling", "Outdoor yoga", "Photography"),
            listOf("T-shirt", "Shorts", "Sunglasses", "Cap", "Sneakers")
        )
        1 -> Triple(
            "Mainly clear",
            listOf("Go jogging", "Read outside", "Fishing", "Play frisbee", "Visit a garden"),
            listOf("Light jacket", "Comfortable pants", "Sneakers", "Hat", "T-shirt")
        )
        2 -> Triple(
            "Partly cloudy",
            listOf("Hiking", "Casual stroll", "Visit a zoo", "Ride a bike", "Do outdoor sketches"),
            listOf("Hoodie", "Jeans", "Hat", "Sneakers", "Layered top")
        )
        3 -> Triple(
            "Overcast",
            listOf("Visit a museum", "Indoor games", "Board games at home", "Watch a movie", "Cook something new"),
            listOf("Sweater", "Long pants", "Sneakers", "Scarf", "Windbreaker")
        )
        48 -> Triple(
            "Depositing rime fog",
            listOf("Stay indoors", "Watch a documentary", "Do indoor yoga", "Read a book", "Warm up with tea"),
            listOf("Thermal wear", "Thick coat", "Gloves", "Boots", "Beanie")
        )
        51, 53, 55 -> Triple(
            "Drizzle",
            listOf("Walk with umbrella", "Cafe hopping", "Visit a bookstore", "Paint indoors", "Listen to music"),
            listOf("Raincoat", "Waterproof boots", "Umbrella", "Jeans", "Sweater")
        )
        61, 63, 65, 80, 81, 82 -> Triple(
            "Rainy",
            listOf("Stay indoors", "Binge a series", "Indoor workout", "Organize your room", "Hot bath"),
            listOf("Rain jacket", "Waterproof boots", "Umbrella", "Cozy clothes", "Socks")
        )
        else -> Triple(
            "Unknown weather",
            listOf("Check forecast later", "Stay prepared"),
            listOf("Dress in layers", "Carry umbrella")
        )
    }
}

// Optional: Returns a drawable resource based on the weather code
fun getWeatherIcon(code: Int): Int {
    return when (code) {
        0 -> R.drawable.cycling
        1 -> R.drawable.cycling
        2 -> R.drawable.cycling
        3 -> R.drawable.cycling
        48 -> R.drawable.cycling
        51, 53, 55 -> R.drawable.cycling
        61, 63, 65, 80, 81, 82 -> R.drawable.cycling
        else -> R.drawable.cycling
    }
}
