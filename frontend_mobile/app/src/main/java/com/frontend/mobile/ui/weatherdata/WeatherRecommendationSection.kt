package com.frontend.mobile.ui.weatherdata

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.frontend.mobile.R

@Composable
fun WeatherRecommendationSection(weatherType: String) {
    val (activityText, iconRes, clothingSuggestion) = when (weatherType.lowercase()) {
        "sunny" -> Triple("Go for a walk or cycling!", R.drawable.walking, "Wear a t-shirt and light pants.")
        "rainy" -> Triple("Maybe stay indoors or bring an umbrella.", R.drawable.coat, "Wear a raincoat.")
        "cloudy" -> Triple("A perfect time for a relaxed walk.", R.drawable.imagecloud, "Bring a light jacket.")
        "windy" -> Triple("Avoid cycling, strong winds!", R.drawable.coat, "Wear something windproof.")
        "cold", "snowy" -> Triple("Go skiing or stay cozy indoors.", R.drawable.coat, "Wear warm clothes.")
        "hot" -> Triple("Stay hydrated and wear sunscreen.", R.drawable.shirt, "Light shirt and shorts.")
        else -> Triple("Check the weather app!", R.drawable.imagecloud, "Be prepared for anything.")
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Activity Suggestion", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(8.dp))
        Image(painter = painterResource(id = iconRes), contentDescription = null, modifier = Modifier.size(100.dp))
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = activityText, style = MaterialTheme.typography.bodyLarge)
        Text(text = clothingSuggestion, style = MaterialTheme.typography.bodyMedium)
    }
}
