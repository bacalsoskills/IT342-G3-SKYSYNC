package com.example.wanderways.ui.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.frontend.mobile.R // make sure imagelogo exists in drawable

data class ActivityRecommendation(
    val title: String,
    val location: String,
    val description: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewAllRecommendedActivities(
    navController: NavController,
    onBackClick: () -> Unit
) {
    val sampleActivities = listOf(
        ActivityRecommendation("Hiking at Mt. Apo", "Davao", "Experience breathtaking views and fresh air."),
        ActivityRecommendation("Surfing in Siargao", "Siargao", "Catch the best waves and meet surfers from around the world."),
        ActivityRecommendation("Snorkeling in Palawan", "Palawan", "Discover marine biodiversity like no other."),
        ActivityRecommendation("Cultural Tour in Vigan", "Ilocos", "Explore preserved Spanish colonial streets.")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Recommended Activities") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.imagelogo),
                    contentDescription = "SkySync Logo",
                    modifier = Modifier
                        .height(140.dp)
                        .padding(bottom = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(sampleActivities) { activity ->
                    ActivityCard(activity)
                }
            }
        }
    }
}

@Composable
fun ActivityCard(activity: ActivityRecommendation) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE0F7FA)),
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(activity.title, style = MaterialTheme.typography.titleMedium)
            Text(activity.location, style = MaterialTheme.typography.labelMedium, color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))
            Text(activity.description, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
