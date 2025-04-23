package com.frontend.mobile.ui.activitypage

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun MyActivityPage(navController: NavHostController, onBackClick: () -> Boolean) {
    val activities = listOf(
        "Booked: Beach Getaway - April 25, 2025",
        "Upcoming: Mountain Hike - May 5, 2025",
        "Past: City Tour - March 12, 2025"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Activities") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                backgroundColor = Color(0xFF1976D2),
                contentColor = Color.White
            )
        },
        content = { padding ->
            if (activities.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No activities found.", fontSize = 16.sp)
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    items(activities) { activity ->
                        ActivityCard(activityText = activity)
                    }
                }
            }
        }
    )
}

@Composable
fun ActivityCard(activityText: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = 6.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = activityText,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF333333)
            )
        }
    }
}
